package nasa.home.ui

import alakazam.kotlin.core.exhaustive
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.LazyColumnScrollbar
import nasa.core.res.CoreDimens
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.scrollbarSettings
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun HomeScreenImpl(
  thumbnailUrls: ThumbnailUrls,
  onAction: (HomeAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = { HomeTopBar(onAction, theme, scrollBehavior) },
  ) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding),
    ) {
      val infiniteTransition = rememberInfiniteTransition(label = "background")
      val targetOffset = with(LocalDensity.current) { TARGET_OFFSET.toPx() }
      val offset by infiniteTransition.animateFloat(
        label = "offset",
        initialValue = 0f,
        targetValue = targetOffset,
        animationSpec = infiniteRepeatable(
          animation = tween(durationMillis = ANIMATION_PERIOD_MS, easing = LinearEasing),
          repeatMode = RepeatMode.Reverse,
        ),
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .blur(BLUR_SIZE)
          .drawWithCache {
            val brushSize = BRUSH_SIZE
            val brush = Brush.linearGradient(
              colors = listOf(theme.pageBackground, theme.pageBackgroundAlt),
              start = Offset(offset, offset),
              end = Offset(offset + brushSize, offset + brushSize),
              tileMode = TileMode.Mirror,
            )
            onDrawBehind { drawRect(brush) }
          },
      )

      HomeScreenContent(
        thumbnailUrls = thumbnailUrls,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

private enum class GridItem {
  Apod,
  Gallery,
}

@Composable
private fun HomeScreenContent(
  thumbnailUrls: ThumbnailUrls,
  onAction: (HomeAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val state = rememberLazyListState()
  LazyColumnScrollbar(
    modifier = modifier
      .fillMaxSize()
      .padding(CoreDimens.medium),
    state = state,
    settings = theme.scrollbarSettings(),
  ) {
    LazyColumn(
      state = state,
      verticalArrangement = Arrangement.Top,
      contentPadding = PaddingValues(GRID_CONTENT_PADDING),
    ) {
      items(GridItem.entries) { item ->
        when (item) {
          GridItem.Apod -> ApodGridItem(
            thumbnailUrl = thumbnailUrls.apod,
            onAction = onAction,
            theme = theme,
          )

          GridItem.Gallery -> GalleryGridItem(
            thumbnailUrl = thumbnailUrls.gallery,
            onAction = onAction,
            theme = theme,
          )
        }.exhaustive
      }
    }
  }
}

private val GRID_CONTENT_PADDING = 0.dp

private val TARGET_OFFSET = 1000.dp
private val BLUR_SIZE = 40.dp
private const val BRUSH_SIZE = 400f
private const val ANIMATION_PERIOD_MS = 30_000

@ScreenPreview
@Composable
private fun PreviewHome() = PreviewScreen {
  HomeScreenImpl(
    thumbnailUrls = PREVIEW_URLS,
    onAction = {},
  )
}
