package apod.single.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.ui.ShimmeringBlock
import apod.core.ui.button.PrimaryIconButton
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn
import apod.single.vm.ApodSingleAction
import apod.single.vm.ScreenState

@Composable
internal fun ItemHeader(
  state: ScreenState,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  if (state is ScreenState.NoApiKey) return

  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(8.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.Top,
  ) {
    PrimaryIconButton(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
      contentDescription = "",
      onClick = {
        state.ifHasDateAndKey { date, key ->
          onAction(ApodSingleAction.LoadPrevious(key, date))
        }
      },
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .wrapContentHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      ItemTitle(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
        state = state,
        theme = theme,
      )

      ItemDate(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
        state = state,
        theme = theme,
      )
    }

    PrimaryIconButton(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = "",
      onClick = {
        state.ifHasDateAndKey { date, key ->
          onAction(ApodSingleAction.LoadNext(key, date))
        }
      },
    )
  }
}

@Composable
private fun ItemTitle(
  state: ScreenState,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  when (state) {
    is ScreenState.NoApiKey, is ScreenState.Loading, ScreenState.Inactive -> {
      ShimmeringBlock(
        modifier = modifier
          .height(20.dp)
          .padding(horizontal = 8.dp, vertical = 0.dp),
        theme = theme,
        color = { pageTextPositiveLoading },
      )
    }

    is ScreenState.Success -> {
      Text(
        text = state.item.title,
        color = theme.pageTextPositive,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
      )
    }

    is ScreenState.Failed -> {
      // Do nothing
    }
  }
}

@Composable
private fun ItemDate(
  state: ScreenState,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val date = when (state) {
    ScreenState.Inactive -> null
    is ScreenState.NoApiKey -> state.date
    is ScreenState.Failed -> state.date
    is ScreenState.Loading -> state.date
    is ScreenState.Success -> state.item.date
  }

  if (date == null) {
    ShimmeringBlock(
      modifier = modifier
        .height(20.dp)
        .padding(horizontal = 8.dp, vertical = 2.dp),
      theme = theme,
      color = { pageTextSubdued },
    )
  } else {
    Text(
      text = date.toString(),
      color = theme.pageTextSubdued,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
    )
  }
}

@Preview
@Composable
private fun PreviewSuccess() = PreviewColumn {
  ItemHeader(
    state = ScreenState.Success(EXAMPLE_ITEM, EXAMPLE_KEY),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewFailure() = PreviewColumn {
  ItemHeader(
    state = ScreenState.Failed(EXAMPLE_DATE, EXAMPLE_KEY, message = "Something broke"),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewLoading() = PreviewColumn {
  ItemHeader(
    state = ScreenState.Loading(EXAMPLE_DATE, EXAMPLE_KEY),
    onAction = {},
  )
}

@Preview
@Composable
private fun PreviewInactive() = PreviewColumn {
  ItemHeader(
    state = ScreenState.Inactive,
    onAction = {},
  )
}
