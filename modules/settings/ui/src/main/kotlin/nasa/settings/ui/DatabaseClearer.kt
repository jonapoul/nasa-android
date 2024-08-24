package nasa.settings.ui

import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import nasa.core.model.FileSize
import nasa.core.model.bytes
import nasa.db.NasaDatabase
import timber.log.Timber
import javax.inject.Inject

internal class DatabaseClearer @Inject constructor(
  private val database: NasaDatabase,
  private val io: IODispatcher,
) {
  private val dbFile = database.file()

  private val mutableFileSize = MutableStateFlow(0.bytes)
  val fileSize: StateFlow<FileSize> = mutableFileSize.asStateFlow()

  init {
    updateFileSize()
  }

  fun updateFileSize() {
    val bytes = dbFile.length().bytes
    mutableFileSize.update { bytes }
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
    updateFileSize()
  }
}
