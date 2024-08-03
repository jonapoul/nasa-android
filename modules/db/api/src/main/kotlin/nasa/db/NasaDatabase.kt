package nasa.db

import java.io.Closeable
import java.io.File

interface NasaDatabase : Closeable {
  override fun close()
  fun clearAllTables()
  fun file(): File
}
