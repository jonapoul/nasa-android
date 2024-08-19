package nasa.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import nasa.db.apod.ApodDao
import nasa.db.apod.ApodEntity
import javax.inject.Inject

@Entity(tableName = "apod")
data class RoomApodEntity(
  @PrimaryKey(autoGenerate = false)
  @ColumnInfo("date")
  override val date: LocalDate,

  @ColumnInfo("title")
  override val title: String,

  @ColumnInfo("explanation")
  override val explanation: String,

  @ColumnInfo("media_type")
  override val mediaType: String,

  @ColumnInfo("copyright")
  override val copyright: String?,

  @ColumnInfo("url")
  override val url: String?,

  @ColumnInfo("hdurl")
  override val hdUrl: String?,

  @ColumnInfo("thumbnail_url")
  override val thumbnailUrl: String?,
) : ApodEntity

@Dao
interface RoomApodDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entity: RoomApodEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entities: List<RoomApodEntity>)

  @Query("SELECT * FROM apod WHERE date = :date LIMIT 1")
  suspend fun get(date: LocalDate): RoomApodEntity?

  @Query("SELECT date FROM apod")
  fun observeDates(): Flow<List<LocalDate>>
}

class RoomApodDaoWrapper @Inject constructor(db: RoomNasaDatabase) : ApodDao {
  private val delegate = db.apodDao()
  override suspend fun insert(entity: ApodEntity) = delegate.insert(entity.toImpl())
  override suspend fun insertAll(entities: List<ApodEntity>) = delegate.insertAll(entities.map { it.toImpl() })
  override suspend fun get(date: LocalDate): ApodEntity? = delegate.get(date)
  override fun observeDates(): Flow<List<LocalDate>> = delegate.observeDates()

  private fun ApodEntity.toImpl(): RoomApodEntity = if (this is RoomApodEntity) {
    this
  } else {
    RoomApodEntity(
      date = date,
      title = title,
      explanation = explanation,
      mediaType = mediaType,
      copyright = copyright,
      url = url,
      hdUrl = hdUrl,
      thumbnailUrl = thumbnailUrl,
    )
  }
}

val DefaultApodEntityFactory = ApodEntity.Factory(::RoomApodEntity)
