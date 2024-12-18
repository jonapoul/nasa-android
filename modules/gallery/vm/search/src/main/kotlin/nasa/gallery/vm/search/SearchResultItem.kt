package nasa.gallery.vm.search

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.gallery.model.Photographer
import nasa.gallery.model.SubtitleUrl

@Immutable
data class SearchResultItem(
  val nasaId: NasaId,
  val collectionUrl: JsonUrl,
  val previewUrl: ImageUrl?,
  val captionsUrl: SubtitleUrl?,
  val albums: ImmutableList<Album>?,
  val center: Center?,
  val title: String,
  val keywords: Keywords?,
  val location: String?,
  val photographer: Photographer?,
  val dateCreated: Instant,
  val mediaType: MediaType,
  val secondaryCreator: String?,
  val description: String?,
  val description508: String?,
)
