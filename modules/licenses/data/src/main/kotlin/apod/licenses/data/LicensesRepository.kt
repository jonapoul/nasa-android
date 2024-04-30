package apod.licenses.data

import alakazam.kotlin.core.DefaultDispatcher
import alakazam.kotlin.core.IODispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import javax.inject.Inject

class LicensesRepository @Inject constructor(
  private val assetsProvider: AssetsProvider,
  private val io: IODispatcher,
  private val default: DefaultDispatcher,
) {
  suspend fun loadLicenses(): LicensesLoadState = try {
    val reader = assetsProvider.licensesJsonStream().reader()
    val jsonString = withContext(io) { reader.use { it.readText() } }

    val dirtyLibraries = withContext(default) {
      LicensesJson.decodeFromString(
        deserializer = ListSerializer(LibraryModel.serializer()),
        string = jsonString,
      )
    }

    val cleanedLibraries = dirtyLibraries
      .sortedBy { it.project }
      .map { it.cleanedUp() }

    LicensesLoadState.Success(cleanedLibraries)
  } catch (e: Exception) {
    LicensesLoadState.Failure(e)
  }
}
