package nasa.db.api

import java.io.File

interface NasaDatabase {
  fun close()
  fun clearAllTables()
  fun file(): File
}
