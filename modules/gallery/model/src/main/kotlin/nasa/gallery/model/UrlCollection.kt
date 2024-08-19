package nasa.gallery.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.setSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable(UrlCollectionSerializer::class)
data class UrlCollection(private val delegate: Set<GalleryUrl>) : Set<GalleryUrl> by delegate {
  fun audioUrls(): AudioUrls? = filterIsInstance<AudioUrl>().ifEmpty { null }?.let(::AudioUrls)
  fun imageUrls(): ImageUrls? = filterIsInstance<ImageUrl>().ifEmpty { null }?.let(::ImageUrls)
  fun videoUrls(): VideoUrls? = filterIsInstance<VideoUrl>().ifEmpty { null }?.let(::VideoUrls)
  fun subtitleUrl(): SubtitleUrl? = filterIsInstance<SubtitleUrl>().firstOrNull()
  fun metadataUrl(): JsonUrl = filterIsInstance<JsonUrl>().first()
}

fun UrlCollection(vararg urls: GalleryUrl) = UrlCollection(urls.toSet())

internal object UrlCollectionSerializer : KSerializer<UrlCollection> {
  override val descriptor = setSerialDescriptor<GalleryUrl>()
  private val stringDescriptor = PrimitiveSerialDescriptor(serialName = "url", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: UrlCollection) {
    encoder.encodeCollection(descriptor, value.size) {
      value.forEachIndexed { index, url ->
        encodeStringElement(stringDescriptor, index, url.toString())
      }
    }
  }

  override fun deserialize(decoder: Decoder): UrlCollection {
    val jsonDecoder = decoder as JsonDecoder
    val urls = jsonDecoder.decodeJsonElement().jsonArray.map { item ->
      val url = item.jsonPrimitive.content
      when (val extension = url.split(".").last().lowercase()) {
        "json" -> JsonUrl(url)
        "srt" -> SubtitleUrl(url)
        "mp4" -> VideoUrl(url)
        "m4a", "mp3" -> AudioUrl(url)
        "jpg", "png" -> ImageUrl(url)
        else -> throw SerializationException("Unknown extension $extension in $url")
      }
    }
    return UrlCollection(urls.toSet())
  }
}
