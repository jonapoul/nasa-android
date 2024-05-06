package apod.data.repo

import alakazam.kotlin.core.IODispatcher
import apod.data.db.ApodDao
import apod.data.db.ApodDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class DatabaseClearer @Inject constructor(
  database: ApodDatabase,
  private val dao: ApodDao,
  private val io: IODispatcher,
) {
  private val dbFile = database.openHelper.readableDatabase.path?.let(::File) ?: error("Null file for $database")

  fun fileSize(): Flow<Long> = dao.itemCount().map { dbFile.length() }

  suspend fun clear(): Boolean = try {
    withContext(io) { dao.clear() }
    true
  } catch (e: Exception) {
    Timber.w(e, "Failed clearing database")
    false
  }
}
