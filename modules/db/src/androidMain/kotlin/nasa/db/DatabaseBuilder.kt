package nasa.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(
  context: Context,
  file: File = NasaDatabase.getFile(context),
): RoomDatabase.Builder<NasaDatabase> = Room.databaseBuilder<NasaDatabase>(
  context = context.applicationContext,
  name = file.absolutePath,
)

fun NasaDatabase.Companion.getFile(context: Context): File = context.applicationContext.getDatabasePath(FILENAME)
