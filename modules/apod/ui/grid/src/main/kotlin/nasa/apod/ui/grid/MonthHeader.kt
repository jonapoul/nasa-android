package nasa.apod.ui.grid

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Month
import nasa.apod.model.ApodNavButtonsState
import nasa.apod.preview.PREVIEW_ITEM_1
import nasa.apod.preview.PREVIEW_ITEM_2
import nasa.apod.res.ApodStrings
import nasa.core.model.ApiKey
import nasa.core.res.CoreDimens
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
      .padding(CoreDimens.large),
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
        .padding(horizontal = CoreDimens.large),
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
fun Month.string(): String = when (this) {
  JavaMonth.JANUARY -> ApodStrings.month1
  JavaMonth.FEBRUARY -> ApodStrings.month2
  JavaMonth.MARCH -> ApodStrings.month3
  JavaMonth.APRIL -> ApodStrings.month4
  JavaMonth.MAY -> ApodStrings.month5
  JavaMonth.JUNE -> ApodStrings.month6
  JavaMonth.JULY -> ApodStrings.month7
  JavaMonth.AUGUST -> ApodStrings.month8
  JavaMonth.SEPTEMBER -> ApodStrings.month9
  JavaMonth.OCTOBER -> ApodStrings.month10
  JavaMonth.NOVEMBER -> ApodStrings.month11
  JavaMonth.DECEMBER -> ApodStrings.month12
}

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  MonthHeader(
    state = GridScreenState.Success(
      persistentListOf(PREVIEW_ITEM_1, PREVIEW_ITEM_2),
      ApiKey.DEMO,
    ),
    navButtons = ApodNavButtonsState.BothEnabled,
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewLoading() = PreviewColumn {
  MonthHeader(
    state = GridScreenState.Loading(date = null, ApiKey.DEMO),
    navButtons = ApodNavButtonsState(enableNext = false, enablePrevious = true),
    onAction = {},
  )
}
