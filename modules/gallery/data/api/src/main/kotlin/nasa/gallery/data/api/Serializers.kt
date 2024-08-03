package nasa.gallery.data.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import nasa.gallery.model.GalleryUrl
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keyword
import nasa.gallery.model.Keywords
import nasa.gallery.model.ListMetadata
import nasa.gallery.model.NullMetadata
import nasa.gallery.model.ObjectMetadata
import nasa.gallery.model.PrimitiveMetadata
import kotlin.reflect.KClass

internal object LocateResponseSerializer :
  CollectionSerializer<LocateResponse, LocateResponse.Success, LocateResponse.Failure>(
    baseClass = LocateResponse::class,
    success = LocateResponse.Success.serializer(),
    failure = LocateResponse.Failure.serializer(),
    successKey = "location",
  )

internal object ManifestResponseSerializer :
  CollectionSerializer<ManifestResponse, ManifestResponse.Success, ManifestResponse.Failure>(
    baseClass = ManifestResponse::class,
    success = ManifestResponse.Success.serializer(),
    failure = ManifestResponse.Failure.serializer(),
  )

internal object SearchResponseSerializer :
  CollectionSerializer<SearchResponse, SearchResponse.Success, SearchResponse.Failure>(
    baseClass = SearchResponse::class,
    success = SearchResponse.Success.serializer(),
    failure = SearchResponse.Failure.serializer(),
  )

internal abstract class CollectionSerializer<B : Any, S : B, F : B>(
  baseClass: KClass<B>,
  private val success: KSerializer<S>,
  private val failure: KSerializer<F>,
  private val successKey: String = "collection",
  private val failureKey: String = "reason",
) : JsonContentPolymorphicSerializer<B>(baseClass) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<B> {
    val jsonObject = element as JsonObject
    return when {
      jsonObject.containsKey(successKey) -> success
      jsonObject.containsKey(failureKey) -> failure
      else -> throw SerializationException("Unknown keys: ${jsonObject.keys}")
    }
  }
}

internal object KeywordsSerializer : KSerializer<Keywords> {
  override val descriptor = buildClassSerialDescriptor("Keywords") { element<String>("Keyword") }

  override fun serialize(encoder: Encoder, value: Keywords) {
    val jsonEncoder = encoder as? JsonEncoder ?: throw SerializationException("Only supports JSON")
    jsonEncoder.encodeJsonElement(
      buildJsonArray {
        value.toList().forEach { keyword ->
          add(keyword.toString())
        }
      },
    )
  }

  override fun deserialize(decoder: Decoder): Keywords {
    val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("Only supports JSON")
    val keywords = jsonDecoder
      .decodeJsonElement()
      .jsonArray
      .flatMap { element ->
        element.jsonPrimitive
          .content
          .split(Keywords.SEPARATOR)
          .map { Keyword(it.trim()) }
      }
    return Keywords(keywords)
  }
}

internal object UrlCollectionSerializer : JsonDeserializer<UrlCollection> {
  override val descriptor = listSerialDescriptor<GalleryUrl>()

  override fun deserialize(root: JsonElement): UrlCollection {
    val urls = root.jsonArray.map { item ->
      val url = item.jsonPrimitive.content
      when {
        url.endsWith(suffix = "json", ignoreCase = true) -> JsonUrl(url)
        else -> ImageUrl(url)
      }
    }
    return UrlCollection(urls)
  }
}

internal object MetadataCollectionSerializer : JsonDeserializer<MetadataCollection> {
  override val descriptor = listSerialDescriptor<GalleryUrl>()

  override fun deserialize(root: JsonElement): MetadataCollection {
    val metadata = root.jsonObject.map { (key, element) ->
      key to when (element) {
        is JsonArray -> ListMetadata(key, element)
        is JsonObject -> ObjectMetadata(key, element)
        is JsonPrimitive -> PrimitiveMetadata(key, element)
        JsonNull -> NullMetadata(key)
      }
    }
    return MetadataCollection(metadata.toMap())
  }
}

internal interface JsonDeserializer<T> : KSerializer<T> {
  fun deserialize(root: JsonElement): T

  override fun serialize(encoder: Encoder, value: T) = throw SerializationException("Serialization is unsupported")

  override fun deserialize(decoder: Decoder): T {
    val jsonDecoder = decoder as JsonDecoder
    return deserialize(jsonDecoder.decodeJsonElement())
  }
}
