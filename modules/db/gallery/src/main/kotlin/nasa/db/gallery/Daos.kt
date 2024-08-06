package nasa.db.gallery

import kotlinx.datetime.LocalDate
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.Keyword
import nasa.gallery.model.Photographer

interface GalleryDao {
  suspend fun insert(entity: GalleryEntity)
  suspend fun insertAll(entities: List<GalleryEntity>)
  suspend fun get(date: LocalDate): GalleryEntity?
  suspend fun clear()
}

interface KeywordDao {
  suspend fun insert(keyword: Keyword)
  suspend fun insertAll(keywords: List<Keyword>)
  suspend fun getAll(): List<Keyword>
  suspend fun clear()
}

interface PhotographerDao {
  suspend fun insert(photographer: Photographer)
  suspend fun insertAll(photographers: List<Photographer>)
  suspend fun getAll(): List<Photographer>
  suspend fun clear()
}

interface CenterDao {
  suspend fun insert(center: Center)
  suspend fun insertAll(centers: List<Center>)
  suspend fun getAll(): List<Center>
  suspend fun clear()
}

interface AlbumDao {
  suspend fun insert(album: Album)
  suspend fun insertAll(albums: List<Album>)
  suspend fun getAll(): List<Album>
  suspend fun clear()
}
