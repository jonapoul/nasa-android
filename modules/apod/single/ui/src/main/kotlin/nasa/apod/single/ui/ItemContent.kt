package nasa.apod.single.ui

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
import androidx.compose.material.icons.filled.Info
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
import coil.compose.AsyncImage
import nasa.apod.model.ApodMediaType
import nasa.apod.res.R
import nasa.core.ui.ShimmerBlockShape
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.screens.LoadFailure
import nasa.core.ui.screens.NoApiKey
import nasa.core.ui.screens.VideoOverlay
import nasa.core.ui.shimmer
import nasa.core.res.R as CoreR

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
          val action = when (state.item.mediaType) {
            ApodMediaType.Image -> ApodSingleAction.ShowImageFullscreen(state.item)
            ApodMediaType.Video -> ApodSingleAction.OpenVideo(state.item.url)
            ApodMediaType.Other -> return@clickable
          }
          onAction(action)
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
    val item = state.item
    val url = when (item.mediaType) {
      ApodMediaType.Image -> item.url
      ApodMediaType.Video -> item.thumbnailUrl ?: item.url
      ApodMediaType.Other -> null
    }

    var isLoading by remember { mutableStateOf(true) }
    if (!url.isNullOrEmpty() && isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp)
          .clip(ShimmerBlockShape)
          .shimmer(theme, durationMillis = 3000),
      )
    }

    if (url.isNullOrEmpty()) {
      NoUrlToLoad(
        modifier = Modifier.wrapContentSize(),
        theme = theme,
      )
    } else {
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

      if (item.mediaType == ApodMediaType.Video) {
        VideoOverlay(
          modifier = Modifier.fillMaxSize(),
          theme = theme,
        )
      }
    }
  }
}

@Composable
private fun NoUrlToLoad(
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
      imageVector = Icons.Filled.Info,
      contentDescription = null,
      tint = theme.warningText,
    )

    VerticalSpacer(10.dp)

    Text(
      text = stringResource(id = CoreR.string.failed_title),
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      color = theme.warningText,
      fontSize = 25.sp,
    )

    VerticalSpacer(10.dp)

    Text(
      text = stringResource(R.string.apod_no_url),
      textAlign = TextAlign.Center,
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
    color = theme.pageTextPrimary,
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
private fun PreviewSuccessVideo() = PreviewScreen {
  val item = EXAMPLE_ITEM.copy(
    thumbnailUrl = EXAMPLE_ITEM.url,
    mediaType = ApodMediaType.Video,
  )
  ItemContent(
    state = ScreenState.Success(item, EXAMPLE_KEY),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewSuccessOther() = PreviewScreen {
  val item = EXAMPLE_ITEM.copy(
    url = "",
    thumbnailUrl = null,
    hdUrl = null,
    mediaType = ApodMediaType.Video,
  )
  ItemContent(
    state = ScreenState.Success(item, EXAMPLE_KEY),
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
