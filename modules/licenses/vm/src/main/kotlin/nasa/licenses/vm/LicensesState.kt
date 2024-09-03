package nasa.licenses.vm

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import nasa.licenses.data.LibraryModel

@Immutable
sealed interface LicensesState {
  data object Loading : LicensesState
  data class Loaded(val libraries: ImmutableList<LibraryModel>) : LicensesState
  data object NoneFound : LicensesState
  data class Error(val errorMessage: String) : LicensesState
}
