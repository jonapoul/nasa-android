package apod.single.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import apod.core.ui.BackgroundSurface
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.single.vm.ApodSingleAction
import apod.single.vm.LoadState

@Composable
internal fun ApodSingleScreenImpl(
  state: LoadState,
  onAction: (ApodSingleAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { ApodViewTopBar(theme, onAction) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      ApodSingleContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodSingleContent(
  state: LoadState,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .background(theme.pageBackground)
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    ItemHeader(
      state = state,
      onAction = onAction,
      modifier = Modifier,
      theme = theme,
    )

    ItemContent(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 8.dp, vertical = 4.dp),
      state = state,
      onAction = onAction,
      theme = theme,
    )
//
//    ItemFooter(
//      modifier = Modifier
//        .wrapContentHeight()
//        .padding(horizontal = 8.dp, vertical = 4.dp),
//      item = item,
//      onAction = onAction,
//      theme = theme,
//    )
  }
}

@ScreenPreview
@Composable
private fun PreviewSingle() = PreviewScreen {
  ApodSingleScreenImpl(
    state = LoadState.Success(EXAMPLE_ITEM),
    onAction = {},
  )
}
