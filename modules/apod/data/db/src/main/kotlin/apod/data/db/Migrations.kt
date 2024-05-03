@file:Suppress("ClassName", "MatchingDeclarationName")

package apod.data.db

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable(tableName = "date")
internal class Migration_1_to_2 : AutoMigrationSpec
