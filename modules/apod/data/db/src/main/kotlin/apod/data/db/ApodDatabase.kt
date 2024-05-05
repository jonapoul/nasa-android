@file:Suppress("ClassName", "MatchingDeclarationName")

package apod.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec

@Database(
  version = 3,
  exportSchema = true,
  entities = [
    ApodEntity::class,
  ],
  autoMigrations = [
    // deleted the "date" table
    AutoMigration(from = 1, to = 2, spec = Migration_1_to_2::class),

    // set the "url" property from non-null to nullable
    AutoMigration(from = 2, to = 3),
  ],
)
@TypeConverters(
  ApodMediaTypeConverter::class,
  LocalDateTypeConverter::class,
)
abstract class ApodDatabase : RoomDatabase() {
  abstract fun apodDao(): ApodDao
}

@DeleteTable(tableName = "date")
internal class Migration_1_to_2 : AutoMigrationSpec
