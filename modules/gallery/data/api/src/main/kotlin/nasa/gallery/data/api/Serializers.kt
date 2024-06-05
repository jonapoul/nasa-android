package nasa.gallery.data.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import nasa.gallery.model.Keyword
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
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

internal object MediaTypeSerializer : KSerializer<MediaType> {
  override val descriptor = PrimitiveSerialDescriptor(serialName = "MediaType", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): MediaType = decoder.decodeString().let(MediaType::parse)
  override fun serialize(encoder: Encoder, value: MediaType) = encoder.encodeString(value.toString())
}

internal object NasaIdSerializer : KSerializer<NasaId> {
  override val descriptor = PrimitiveSerialDescriptor(serialName = "NasaId", PrimitiveKind.STRING)
  override fun deserialize(decoder: Decoder): NasaId = decoder.decodeString().let(::NasaId)
  override fun serialize(encoder: Encoder, value: NasaId) = encoder.encodeString(value.toString())
}

internal object KeywordsSerializer : KSerializer<Keywords> {
  override val descriptor = buildClassSerialDescriptor("Keywords") { element<String>("Keyword") }

  override fun serialize(encoder: Encoder, value: Keywords) {
    val jsonEncoder = encoder as? JsonEncoder ?: error("Only supports JSON")
    jsonEncoder.encodeJsonElement(
      buildJsonArray {
        value.toList().forEach { keyword ->
          add(keyword.toString())
        }
      },
    )
  }

  override fun deserialize(decoder: Decoder): Keywords {
    val jsonDecoder = decoder as? JsonDecoder ?: error("Only supports JSON")
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
