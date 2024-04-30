@file:OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)

package apod.data.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

internal object SingleResponseSerializer : KSerializer<SingleApodResponse> {
  override val descriptor = buildSerialDescriptor(serialName = "SingleApodResponse", PolymorphicKind.SEALED)

  override fun serialize(encoder: Encoder, value: SingleApodResponse) = error("Serialization is unsupported for $value")

  override fun deserialize(decoder: Decoder): SingleApodResponse {
    val input = decoder as? JsonDecoder ?: error("Invalid decoder $decoder")
    val json = input.json
    val rootElement = input.decodeJsonElement()
    val rootObject = rootElement as? JsonObject ?: error("Unrecognised format for $rootElement")

    val errorObject = rootObject["error"]
    return if (errorObject != null) {
      json.decodeFromJsonElement(SingleApodResponse.Failure.serializer(), errorObject)
    } else {
      val item = json.decodeFromJsonElement(ApodResponseItem.serializer(), rootObject)
      SingleApodResponse.Success(item)
    }
  }
}

internal object MultipleResponseSerializer : KSerializer<MultipleApodResponse> {
  override val descriptor = buildSerialDescriptor(serialName = "MultipleApodResponse", PolymorphicKind.SEALED)

  override fun serialize(encoder: Encoder, value: MultipleApodResponse) =
    error("Serialization is unsupported for $value")

  override fun deserialize(decoder: Decoder): MultipleApodResponse {
    val input = decoder as? JsonDecoder ?: error("Invalid decoder $decoder")
    val json = input.json
    val rootElement = input.decodeJsonElement()
    val rootObject = rootElement as? JsonObject ?: error("Unrecognised format for $rootElement")

    val errorObject = rootObject["error"]
    return if (errorObject != null) {
      json.decodeFromJsonElement(MultipleApodResponse.Failure.serializer(), errorObject)
    } else {
      val serializer = ListSerializer(ApodResponseItem.serializer())
      val items = json.decodeFromJsonElement(serializer, rootObject)
      MultipleApodResponse.Success(items)
    }
  }
}

internal object LocalDateSerializer : KSerializer<LocalDate> {
  override val descriptor = PrimitiveSerialDescriptor(serialName = "ApodDate", PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
  override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}
