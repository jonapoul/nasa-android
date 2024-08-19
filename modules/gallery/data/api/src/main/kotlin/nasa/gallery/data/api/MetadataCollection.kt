package nasa.gallery.data.api

import kotlinx.serialization.Serializable
import nasa.gallery.model.Metadata

@Serializable(MetadataCollectionSerializer::class)
data class MetadataCollection(private val delegate: Map<String, Metadata<*>>) : Map<String, Metadata<*>> by delegate
