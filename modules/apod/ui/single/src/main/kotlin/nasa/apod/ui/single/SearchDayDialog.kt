package nasa.apod.ui.single

import android.text.format.DateUtils
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.res.R
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.datePicker

@Composable
internal fun SearchDayDialog(
  initialDate: LocalDate,
  onConfirm: (LocalDate) -> Unit,
  onCancel: () -> Unit,
  theme: Theme = LocalTheme.current,
) {
  val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
  val yearRange = remember { EARLIEST_APOD_DATE.year..today.year }
  val colors = theme.datePicker()

  val state = rememberDatePickerState(
    initialSelectedDateMillis = initialDate.toEpochDays() * DateUtils.DAY_IN_MILLIS,
    yearRange = yearRange,
    initialDisplayMode = DisplayMode.Picker,
    selectableDates = ApodSelectableDates(today, yearRange),
  )
  val selectedDate = state.selectedDateMillis?.let { ms ->
    val epochDays = ms / DateUtils.DAY_IN_MILLIS
    LocalDate.fromEpochDays(epochDays.toInt())
  }

  DatePickerDialog(
    onDismissRequest = onCancel,
    colors = colors,
    shape = RoundedCornerShape(0.dp),
    dismissButton = {
      TextButton(onClick = onCancel) {
        Text(
          text = stringResource(R.string.search_dialog_cancel),
          color = theme.pageTextPrimary,
        )
      }
    },
    confirmButton = {
      TextButton(onClick = { onConfirm(selectedDate ?: initialDate) }) {
        Text(
          text = stringResource(R.string.search_dialog_confirm),
          color = theme.pageTextPrimary,
        )
      }
    },
  ) {
    DatePicker(
      state = state,
      modifier = Modifier.fillMaxWidth(),
      colors = colors,
    )
  }
}

@Stable
private data class ApodSelectableDates(val today: LocalDate, val yearRange: IntRange) : SelectableDates {
  private val todayMs = today.toEpochDays() * DateUtils.DAY_IN_MILLIS
  private val earliestMs = EARLIEST_APOD_DATE.toEpochDays() * DateUtils.DAY_IN_MILLIS

  override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis in earliestMs..todayMs
  override fun isSelectableYear(year: Int): Boolean = year in yearRange
}
