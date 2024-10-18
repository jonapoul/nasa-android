package nasa.apod.ui.grid

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import nasa.apod.model.EARLIEST_APOD_DATE
import nasa.apod.preview.PREVIEW_DATE
import nasa.apod.res.R
import nasa.core.ui.Dimensions
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.datePicker
import nasa.core.ui.preview.PreviewColumn
import nasa.core.ui.text.NasaExposedDropDownMenu

@Composable
internal fun SearchMonthDialog(
  initialDate: LocalDate,
  onConfirm: (LocalDate) -> Unit,
  onCancel: () -> Unit,
  theme: Theme = LocalTheme.current,
) {
  val colors = theme.datePicker()
  var selectedDate by remember { mutableStateOf(initialDate) }
  var enableConfirmButton by remember { mutableStateOf(true) }

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
      TextButton(
        enabled = enableConfirmButton,
        onClick = { onConfirm(selectedDate) },
      ) {
        Text(
          text = stringResource(R.string.search_dialog_confirm),
          color = if (enableConfirmButton) theme.pageTextPrimary else theme.pageTextSubdued,
        )
      }
    },
  ) {
    MonthYearPicker(
      modifier = Modifier.fillMaxWidth(),
      selectedDate = selectedDate,
      onSelectedDate = { selectedDate = it },
      onDateIsValid = { enableConfirmButton = it },
      theme = theme,
    )
  }
}

/**
 * This could probably be tidied up a bit more
 */
@Composable
private fun MonthYearPicker(
  selectedDate: LocalDate,
  onSelectedDate: (LocalDate) -> Unit,
  onDateIsValid: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  minDate: LocalDate = EARLIEST_APOD_DATE,
  theme: Theme = LocalTheme.current,
) {
  val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
  val dateRange = remember { LocalDate(minDate.year, minDate.month, dayOfMonth = 1)..today }
  val yearRange = remember { minDate.year..today.year }

  val months = remember { Month.entries.toImmutableList() }
  val years = remember { yearRange.toImmutableList() }

  val monthStrings = remember { months.map { it.capitalized() }.toImmutableList() }
  val yearStrings = remember { years.map { it.toString() }.toImmutableList() }

  var month by remember { mutableStateOf(selectedDate.month) }
  var selectedMonthIndex by remember { mutableIntStateOf(selectedDate.monthNumber - 1) }

  var year by remember { mutableIntStateOf(selectedDate.year) }
  var selectedYearIndex by remember { mutableIntStateOf(yearStrings.indexOf(year.toString())) }

  var date by remember { mutableStateOf(selectedDate) }

  Column(
    modifier = modifier
      .background(theme.dialogBackground)
      .padding(
        start = Dimensions.Large,
        end = Dimensions.Large,
        top = Dimensions.Large,
        bottom = Dimensions.Large,
      ),
  ) {
    Text(
      text = stringResource(R.string.search_dialog_title),
      color = theme.pageTextPrimary,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
    )

    VerticalSpacer(10.dp)

    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      NasaExposedDropDownMenu(
        modifier = Modifier.weight(1f),
        theme = theme,
        value = monthStrings[selectedMonthIndex],
        options = monthStrings,
        onValueChange = {
          selectedMonthIndex = monthStrings.indexOf(it)
          month = Month(number = selectedMonthIndex + 1)
          date = LocalDate(year, month, dayOfMonth = 1)
          onSelectedDate(date)
          onDateIsValid(date in dateRange)
        },
      )

      HorizontalSpacer(10.dp)

      NasaExposedDropDownMenu(
        modifier = Modifier.weight(1f),
        theme = theme,
        value = yearStrings[selectedYearIndex],
        options = yearStrings,
        onValueChange = {
          selectedYearIndex = yearStrings.indexOf(it)
          year = yearStrings[selectedYearIndex].toInt()
          date = LocalDate(year, month, dayOfMonth = 1)
          onSelectedDate(date)
          onDateIsValid(date in dateRange)
        },
      )
    }

    if (date !in dateRange) {
      VerticalSpacer(10.dp)

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector = Icons.Filled.Warning,
          contentDescription = null,
          tint = theme.warningText,
        )

        HorizontalSpacer(10.dp)

        val thisMonth = "${today.month.capitalized()} ${today.year}"
        Text(
          text = stringResource(R.string.search_dialog_out_of_range, thisMonth),
          color = theme.warningText,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
        )
      }
    }
  }
}

@Stable
private fun Month.capitalized(): String = name.lowercase().replaceFirstChar { it.uppercase() }

@Preview
@Composable
private fun PreviewMonthPicker() = PreviewColumn {
  var selected by remember { mutableStateOf(PREVIEW_DATE) }
  MonthYearPicker(
    modifier = Modifier.wrapContentHeight(),
    selectedDate = selected,
    onSelectedDate = { selected = it },
    onDateIsValid = {},
  )
}

@Preview
@Composable
private fun PreviewOutOfRange() = PreviewColumn {
  val date = LocalDate(year = 1995, monthNumber = 1, dayOfMonth = 1)
  var selected by remember { mutableStateOf(date) }
  MonthYearPicker(
    modifier = Modifier.wrapContentHeight(),
    selectedDate = selected,
    onSelectedDate = { selected = it },
    onDateIsValid = {},
  )
}
