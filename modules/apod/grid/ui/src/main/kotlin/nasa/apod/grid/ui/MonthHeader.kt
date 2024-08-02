package nasa.apod.grid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Month
import nasa.apod.grid.vm.GridScreenState
import nasa.apod.grid.vm.dateOrNull
import nasa.apod.model.ApodNavButtonsState
import nasa.core.ui.ShimmeringBlock
import nasa.core.ui.button.PrimaryIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import java.time.Month as JavaMonth

@Composable
internal fun MonthHeader(
  state: GridScreenState,
  navButtons: ApodNavButtonsState,
  onAction: (ApodGridAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  if (state is GridScreenState.NoApiKey) {
    return
  }

  Row(
    modifier = modifier
      .background(theme.toolbarBackgroundSubdued)
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(8.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    PrimaryIconButton(
      modifier = Modifier.wrapContentSize(),
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
      contentDescription = "",
      enabled = navButtons.enablePrevious,
      onClick = {
        state.ifHasDate { date ->
          onAction(ApodGridAction.LoadPrevious(date))
        }
      },
    )

    MonthTitle(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 8.dp),
      state = state,
      theme = theme,
    )

    PrimaryIconButton(
      modifier = Modifier.wrapContentSize(),
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = "",
      enabled = navButtons.enableNext,
      onClick = {
        state.ifHasDate { date ->
          onAction(ApodGridAction.LoadNext(date))
        }
      },
    )
  }
}

@Composable
private fun MonthTitle(
  state: GridScreenState,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val date = state.dateOrNull()
  if (date == null) {
    ShimmeringBlock(
      modifier = modifier.height(40.dp),
      theme = theme,
      color = { pageTextPrimary },
    )
  } else {
    val month = date.month.string()
    val year = date.year
    Text(
      modifier = modifier,
      text = "$month $year",
      color = theme.toolbarText,
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
    )
  }
}

@Stable
@Composable
fun Month.string(): String = stringResource(
  when (this) {
    JavaMonth.JANUARY -> R.string.month_1
    JavaMonth.FEBRUARY -> R.string.month_2
    JavaMonth.MARCH -> R.string.month_3
    JavaMonth.APRIL -> R.string.month_4
    JavaMonth.MAY -> R.string.month_5
    JavaMonth.JUNE -> R.string.month_6
    JavaMonth.JULY -> R.string.month_7
    JavaMonth.AUGUST -> R.string.month_8
    JavaMonth.SEPTEMBER -> R.string.month_9
    JavaMonth.OCTOBER -> R.string.month_10
    JavaMonth.NOVEMBER -> R.string.month_11
    JavaMonth.DECEMBER -> R.string.month_12
  },
)

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  MonthHeader(
    state = GridScreenState.Success(EXAMPLE_ITEMS, EXAMPLE_KEY),
    navButtons = ApodNavButtonsState.BothEnabled,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewLoading() = PreviewColumn {
  MonthHeader(
    state = GridScreenState.Loading(date = null, EXAMPLE_KEY),
    navButtons = ApodNavButtonsState(enableNext = false, enablePrevious = true),
    onAction = {},
  )
}
