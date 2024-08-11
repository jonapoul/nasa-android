package nasa.gallery.data.api

import kotlinx.serialization.Serializable
import nasa.gallery.model.GalleryUrl
import nasa.gallery.model.Metadata

@Serializable(UrlCollectionSerializer::class)
data class UrlCollection(private val delegate: Set<GalleryUrl>) : Set<GalleryUrl> by delegate

fun UrlCollection(vararg urls: GalleryUrl) = UrlCollection(urls.toSet())

@Serializable(MetadataCollectionSerializer::class)
data class MetadataCollection(private val delegate: Map<String, Metadata<*>>) : Map<String, Metadata<*>> by delegate
