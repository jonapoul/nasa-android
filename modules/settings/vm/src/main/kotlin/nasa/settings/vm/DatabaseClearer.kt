package nasa.settings.vm

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import nasa.db.NasaDatabase
import timber.log.Timber
import java.io.File
import javax.inject.Inject

internal class DatabaseClearer @Inject constructor(
  private val database: NasaDatabase,
  private val io: IODispatcher,
) {
  private val dbFile = database
    .openHelper
    .readableDatabase
    .path
    ?.let(::File) ?: error("Null file for $database")

  private val mutableFileSize = MutableStateFlow(0.bytes)
  val fileSize: StateFlow<FileSize> = mutableFileSize.asStateFlow()

  init {
    updateFileSize()
  }

  fun updateFileSize() {
    mutableFileSize.update { dbFile.length().bytes }
  }

  suspend fun clear(): Boolean = try {
    withContext(io) {
      database.close()
      database.clearAllTables()
    }
    true
  } catch (e: Exception) {
    Timber.w(e, "Failed clearing database")
    false
  } finally {
    runCatching { dbFile.delete() }
    mutableFileSize.update { dbFile.length().bytes }
  }
}
