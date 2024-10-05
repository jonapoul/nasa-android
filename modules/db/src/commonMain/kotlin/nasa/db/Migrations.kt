package nasa.db

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn(tableName = "gallery", fromColumnName = "date", toColumnName = "id")
@DeleteColumn(tableName = "gallery", columnName = "date")
internal class Migrate3to4 : AutoMigrationSpec

@DeleteTable(tableName = "album")
@DeleteTable(tableName = "center")
@DeleteTable(tableName = "keyword")
@DeleteTable(tableName = "photographer")
@DeleteTable(tableName = "url")
internal class Migrate4to5 : AutoMigrationSpec
