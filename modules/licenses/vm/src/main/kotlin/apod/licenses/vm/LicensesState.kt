package apod.licenses.vm

import androidx.compose.runtime.Immutable
import apod.licenses.data.LibraryModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed interface LicensesState {
  data object Loading : LicensesState
  data class Loaded(val libraries: ImmutableList<LibraryModel>) : LicensesState
  data object NoneFound : LicensesState
  data class Error(val errorMessage: String) : LicensesState
}
