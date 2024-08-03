package nasa.gallery.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

sealed interface Metadata<T> {
  val key: String
  val value: T
}

// Basic types
sealed interface PrimitiveMetadata<T> : Metadata<T>

data class BooleanMetadata(override val key: String, override val value: Boolean) : PrimitiveMetadata<Boolean>

data class DoubleMetadata(override val key: String, override val value: Double) : PrimitiveMetadata<Double>

data class IntMetadata(override val key: String, override val value: Int) : PrimitiveMetadata<Int>

data class LongMetadata(override val key: String, override val value: Long) : PrimitiveMetadata<Long>

data class StringMetadata(override val key: String, override val value: String) : PrimitiveMetadata<String>

// Collection types
sealed interface ListMetadata<T> : Metadata<ImmutableList<T>>

data class ObjectListMetadata(
  override val key: String,
  override val value: ImmutableList<Object>,
) : ListMetadata<Object>

data class StringListMetadata(
  override val key: String,
  override val value: ImmutableList<String>,
) : ListMetadata<String>

// Other types
typealias Object = ImmutableMap<String, Any>

data class ObjectMetadata(override val key: String, override val value: Object) : Metadata<Object>

data class NullMetadata(override val key: String) : Metadata<JsonNull> {
  override val value = JsonNull
}

fun PrimitiveMetadata(key: String, primitive: JsonPrimitive): PrimitiveMetadata<*> {
  if (primitive.isString) return StringMetadata(key, primitive.content)
  return primitive.intOrNull?.let { IntMetadata(key, it) }
    ?: primitive.longOrNull?.let { LongMetadata(key, it) }
    ?: primitive.booleanOrNull?.let { BooleanMetadata(key, it) }
    ?: primitive.doubleOrNull?.let { DoubleMetadata(key, it) }
    ?: throw SerializationException("Unexpected primitive type $primitive")
}

fun ListMetadata(key: String, array: JsonArray): ListMetadata<*> {
  val primitives = array.filterIsInstance<JsonPrimitive>()
  if (primitives.isNotEmpty()) return StringListMetadata(key, primitives.map { it.content }.toImmutableList())
  val objects = array.filterIsInstance<JsonObject>()
  if (objects.isNotEmpty()) return ObjectListMetadata(key, objects.map(::toObject).toImmutableList())
  throw SerializationException("Unsupported array $array")
}

fun ObjectMetadata(key: String, jsonObject: JsonObject): ObjectMetadata {
  return ObjectMetadata(key, toObject(jsonObject))
}

private fun toObject(jsonObject: JsonObject): Object = jsonObject
  .map { (key, element) -> key to element.toPrimitiveOrThrow() }
  .toMap()
  .toImmutableMap()

private fun JsonElement.toPrimitiveOrThrow(): Any {
  val primitive = jsonPrimitive
  return if (primitive.isString) {
    primitive.content
  } else {
    primitive.intOrNull
      ?: primitive.longOrNull
      ?: primitive.booleanOrNull
      ?: primitive.doubleOrNull
      ?: throw SerializationException("No valid primitive in $primitive")
  }
}
