package apod.single.ui

import alakazam.android.ui.compose.VerticalSpacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.model.ApodMediaType
import apod.core.ui.ShimmerBlockShape
import apod.core.ui.button.PrimaryTextButton
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.core.ui.shimmer
import apod.single.res.R
import apod.single.vm.ApodSingleAction
import apod.single.vm.ScreenState
import coil.compose.AsyncImage

@Composable
internal fun ItemContent(
  state: ScreenState,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
      .clickable(enabled = state is ScreenState.Success) {
        if (state is ScreenState.Success) {
          onAction(ApodSingleAction.ShowImageFullscreen(state.item))
        }
      },
    contentAlignment = Alignment.Center,
  ) {
    when (state) {
      ScreenState.Inactive, is ScreenState.Loading -> {
        ItemContentLoading(
          modifier = Modifier.wrapContentSize(),
          theme = theme,
        )
      }

      is ScreenState.Failed -> {
        ItemContentFailure(
          modifier = Modifier.fillMaxSize(),
          state = state,
          onAction = onAction,
          theme = theme,
        )
      }

      is ScreenState.Success -> {
        ItemContentSuccess(
          modifier = Modifier.fillMaxSize(),
          state = state,
        )
      }
    }
  }
}

@Composable
private fun ItemContentSuccess(
  state: ScreenState.Success,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    var isLoading by remember { mutableStateOf(true) }
    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp)
          .clip(ShimmerBlockShape)
          .shimmer(theme, durationMillis = 3000),
      )
    }

    val item = state.item
    val url = when (item.mediaType) {
      ApodMediaType.Image -> item.url
      ApodMediaType.Video -> item.thumbnailUrl
      ApodMediaType.Other -> item.thumbnailUrl ?: item.url
    }

    val fallback = rememberVectorPainter(Icons.Filled.Warning)
    AsyncImage(
      modifier = Modifier.fillMaxSize(),
      model = url,
      contentDescription = item.title,
      contentScale = ContentScale.Fit,
      alignment = Alignment.Center,
      fallback = fallback,
      onLoading = { isLoading = true },
      onSuccess = { isLoading = false },
      onError = { isLoading = false },
    )
  }
}

@Composable
private fun ItemContentFailure(
  state: ScreenState.Failed,
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
    state = ScreenState.Success(EXAMPLE_ITEM),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  ItemContent(
    state = ScreenState.Failed(EXAMPLE_DATE, message = "Something broke! Here's some more rubbish too for preview"),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ItemContent(
    state = ScreenState.Loading(EXAMPLE_DATE),
    onAction = {},
  )
}
