package nasa.db.gallery

import kotlinx.coroutines.flow.Flow
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.Keyword
import nasa.gallery.model.NasaId
import nasa.gallery.model.Photographer
import nasa.gallery.model.UrlCollection

interface GalleryDao {
  suspend fun insert(entity: GalleryEntity)
  suspend fun insert(entities: List<GalleryEntity>)
  suspend fun get(id: NasaId): GalleryEntity?
  suspend fun delete(id: NasaId)
}

interface KeywordDao {
  suspend fun insert(keyword: Keyword)
  suspend fun insert(keywords: List<Keyword>)
}

interface PhotographerDao {
  suspend fun insert(photographer: Photographer)
  suspend fun insert(photographers: List<Photographer>)
}

interface CenterDao {
  suspend fun insert(center: Center)
  suspend fun insert(centers: List<Center>)
}

interface AlbumDao {
  suspend fun insert(album: Album)
  suspend fun insert(albums: List<Album>)
}

interface UrlDao {
  suspend fun insert(id: NasaId, urls: UrlCollection)
  suspend fun get(id: NasaId): UrlCollection?
  fun observe(): Flow<List<UrlCollection>>
}
