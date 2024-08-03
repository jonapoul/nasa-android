package nasa.db

import java.io.File

class NasaDatabaseDelegate(private val impl: RoomNasaDatabase) : NasaDatabase {
  override fun close() = impl.close()

  override fun clearAllTables() = impl.clearAllTables()

  override fun file() = impl.openHelper.readableDatabase.path
    ?.let(::File) ?: error("Null file for database $impl")
}
