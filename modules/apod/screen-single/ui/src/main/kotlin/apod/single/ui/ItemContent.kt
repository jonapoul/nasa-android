package apod.single.ui

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.ui.button.PrimaryTextButton
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.single.res.R
import apod.single.vm.ApodSingleAction
import apod.single.vm.LoadState
import apod.core.res.R as CoreR

@Composable
internal fun ItemContent(
  state: LoadState,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .clickable(enabled = state is LoadState.Success) {
        if (state is LoadState.Success) {
          onAction(ApodSingleAction.ShowImageFullscreen(state.item))
        }
      },
    contentAlignment = Alignment.Center,
  ) {
    when (state) {
      LoadState.Inactive, is LoadState.Loading -> {
        ItemContentLoading(
          modifier = Modifier.wrapContentSize(),
          theme = theme,
        )
      }

      is LoadState.Failed -> {
        ItemContentFailure(
          modifier = Modifier.fillMaxSize(),
          state = state,
          onAction = onAction,
          theme = theme,
        )
      }

      is LoadState.Success -> {
        ItemContentSuccess(
          modifier = Modifier.fillMaxSize(),
          state = state,
          theme = theme,
        )
      }
    }
  }
}

@Suppress("UNUSED_PARAMETER", "UnusedParameter")
@Composable
private fun ItemContentSuccess(
  state: LoadState.Success,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Image(
    modifier = modifier.fillMaxSize(),
    painter = painterResource(id = CoreR.mipmap.app_icon_round), // TODO: replace
    contentDescription = null,
    contentScale = ContentScale.Fit,
    alignment = Alignment.Center,
  )
}

@Composable
private fun ItemContentFailure(
  state: LoadState.Failed,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .wrapContentSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(
      modifier = Modifier.size(70.dp),
      imageVector = Icons.Filled.Warning,
      contentDescription = null,
      tint = theme.errorText,
    )

    VerticalSpacer(10.dp)

    Text(
      text = stringResource(id = R.string.apod_failed_title),
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      color = theme.errorText,
      fontSize = 25.sp,
    )

    VerticalSpacer(10.dp)

    Text(
      text = state.message,
      textAlign = TextAlign.Center,
    )

    VerticalSpacer(20.dp)

    PrimaryTextButton(
      text = stringResource(id = R.string.apod_failed_retry),
      onClick = { onAction(ApodSingleAction.RetryLoad(state.date)) },
    )
  }
}

@Composable
private fun ItemContentLoading(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  CircularProgressIndicator(
    modifier = modifier,
    color = theme.pageTextPositive,
    trackColor = theme.dialogProgressWheelTrack,
  )
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ItemContent(
    state = LoadState.Success(EXAMPLE_ITEM),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  ItemContent(
    state = LoadState.Failed(EXAMPLE_DATE, message = "Something broke! Here's some more rubbish too for preview"),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ItemContent(
    state = LoadState.Loading(EXAMPLE_DATE),
    onAction = {},
  )
}
