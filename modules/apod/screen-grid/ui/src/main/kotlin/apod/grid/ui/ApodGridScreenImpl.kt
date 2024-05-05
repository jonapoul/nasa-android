package apod.grid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
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
import apod.core.ui.screens.LoadFailure
import apod.core.ui.screens.NoApiKey
import apod.grid.vm.ApodGridAction
import apod.grid.vm.GridScreenState

@Composable
internal fun ApodGridScreenImpl(
  state: GridScreenState,
  showBackButton: Boolean,
  onAction: (ApodGridAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { ApodGridTopBar(state, showBackButton, onAction, theme) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      ApodGridContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun ApodGridContent(
  state: GridScreenState,
  onAction: (ApodGridAction) -> Unit,
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
    MonthHeader(
      state = state,
      onAction = onAction,
      theme = theme,
    )

    GridContent(
      modifier = Modifier.weight(1f),
      state = state,
      onAction = onAction,
      theme = theme,
    )
  }
}

@Composable
private fun GridContent(
  state: GridScreenState,
  onAction: (ApodGridAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    contentAlignment = Alignment.Center,
  ) {
    when (state) {
      is GridScreenState.NoApiKey -> {
        NoApiKey(
          modifier = Modifier.fillMaxSize(),
          onClickRegister = { onAction(ApodGridAction.RegisterForApiKey) },
          onClickSettings = { onAction(ApodGridAction.NavSettings) },
          theme = theme,
        )
      }

      GridScreenState.Inactive, is GridScreenState.Loading -> {
        CircularProgressIndicator(
          modifier = Modifier.wrapContentSize(),
          color = theme.pageTextPrimary,
          trackColor = theme.dialogProgressWheelTrack,
        )
      }

      is GridScreenState.Failed -> {
        LoadFailure(
          modifier = Modifier.fillMaxSize(),
          message = state.message,
          onRetryLoad = { onAction(ApodGridAction.RetryLoad(state.key, state.date)) },
          theme = theme,
        )
      }

      is GridScreenState.Success -> {
        LazyVerticalGrid(
          modifier = Modifier.fillMaxSize(),
          columns = GridCells.Adaptive(ITEM_SIZE),
          verticalArrangement = Arrangement.Top,
          horizontalArrangement = Arrangement.Center,
        ) {
          items(state.items) { item ->
            ApodGridItem(
              item = item,
              onAction = onAction,
              modifier = Modifier,
              theme = theme,
            )
          }
        }
      }
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSuccess() = PreviewScreen {
  ApodGridScreenImpl(
    state = GridScreenState.Success(EXAMPLE_ITEMS, EXAMPLE_KEY),
    showBackButton = true,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewFailure() = PreviewScreen {
  ApodGridScreenImpl(
    state = GridScreenState.Failed(
      EXAMPLE_DATE,
      EXAMPLE_KEY,
      message = "Something broke! Here's some more rubbish too for preview",
    ),
    showBackButton = true,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ApodGridScreenImpl(
    state = GridScreenState.Loading(EXAMPLE_DATE, EXAMPLE_KEY),
    showBackButton = true,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoKey() = PreviewScreen {
  ApodGridScreenImpl(
    state = GridScreenState.NoApiKey,
    showBackButton = true,
    onAction = {},
  )
}
