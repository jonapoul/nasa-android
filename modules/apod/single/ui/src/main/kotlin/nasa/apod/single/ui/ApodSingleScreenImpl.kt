package nasa.apod.single.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nasa.apod.model.ApodNavButtonsState
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun ApodSingleScreenImpl(
  state: ScreenState,
  navButtons: ApodNavButtonsState,
  showBackButton: Boolean,
  onAction: (ApodSingleAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { ApodSingleTopBar(state, showBackButton, onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      ApodSingleContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        navButtons = navButtons,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodSingleContent(
  state: ScreenState,
  navButtons: ApodNavButtonsState,
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
      navButtons = navButtons,
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

    if (state is ScreenState.Success) {
      ItemFooter(
        modifier = Modifier
          .wrapContentHeight()
          .padding(horizontal = 8.dp, vertical = 4.dp),
        item = state.item,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodSingleScreenImpl(
    state = ScreenState.Success(EXAMPLE_ITEM, EXAMPLE_KEY),
    navButtons = ApodNavButtonsState.BothEnabled,
    onAction = {},
    showBackButton = false,
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  ApodSingleScreenImpl(
    state = ScreenState.Failed(
      EXAMPLE_DATE,
      EXAMPLE_KEY,
      message = "Something broke! Here's some more rubbish too for preview",
    ),
    navButtons = ApodNavButtonsState(enableNext = false, enablePrevious = true),
    onAction = {},
    showBackButton = true,
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ApodSingleScreenImpl(
    state = ScreenState.Loading(EXAMPLE_DATE, EXAMPLE_KEY),
    navButtons = ApodNavButtonsState(enableNext = true, enablePrevious = false),
    onAction = {},
    showBackButton = false,
  )
}

@ScreenPreview
@Composable
private fun PreviewNoApiKey() = PreviewScreen {
  ApodSingleScreenImpl(
    state = ScreenState.NoApiKey(EXAMPLE_DATE),
    navButtons = ApodNavButtonsState.BothDisabled,
    onAction = {},
    showBackButton = false,
  )
}
