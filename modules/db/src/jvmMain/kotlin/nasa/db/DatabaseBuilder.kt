package nasa.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getDatabaseBuilder(
  directory: File,
  file: File = directory.resolve(NasaDatabase.FILENAME),
): RoomDatabase.Builder<NasaDatabase> = Room
  .databaseBuilder<NasaDatabase>(file.absolutePath)
  .setDriver(BundledSQLiteDriver())
