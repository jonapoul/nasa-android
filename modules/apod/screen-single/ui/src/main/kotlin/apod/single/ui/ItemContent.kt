package apod.single.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import apod.core.model.ApodMediaType
import apod.core.ui.ShimmerBlockShape
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.core.ui.screens.LoadFailure
import apod.core.ui.screens.NoApiKey
import apod.core.ui.shimmer
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
      is ScreenState.NoApiKey -> {
        NoApiKey(
          modifier = Modifier.fillMaxSize(),
          onClickRegister = { onAction(ApodSingleAction.RegisterForApiKey) },
          onClickSettings = { onAction(ApodSingleAction.NavSettings) },
          theme = theme,
        )
      }

      ScreenState.Inactive, is ScreenState.Loading -> {
        ItemContentLoading(
          modifier = Modifier.wrapContentSize(),
          theme = theme,
        )
      }

      is ScreenState.Failed -> {
        LoadFailure(
          modifier = Modifier.fillMaxSize(),
          message = state.message,
          onRetryLoad = { onAction(ApodSingleAction.RetryLoad(state.key, state.date)) },
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
    state = ScreenState.Success(EXAMPLE_ITEM, EXAMPLE_KEY),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  ItemContent(
    state = ScreenState.Failed(
      EXAMPLE_DATE,
      EXAMPLE_KEY,
      message = "Something broke! Here's some more rubbish too for preview",
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ItemContent(
    state = ScreenState.Loading(EXAMPLE_DATE, EXAMPLE_KEY),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoApiKey() = PreviewScreen {
  ItemContent(
    state = ScreenState.NoApiKey(EXAMPLE_DATE),
    onAction = {},
  )
}
