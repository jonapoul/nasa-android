package nasa.db.gallery

import kotlinx.datetime.LocalDate

interface GalleryDao {
  suspend fun insert(entity: GalleryEntity)
  suspend fun insertAll(entities: List<GalleryEntity>)
  suspend fun get(date: LocalDate): GalleryEntity?
  suspend fun clear()
}
