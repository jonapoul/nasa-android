package apod.about.ui

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import apod.about.vm.AboutAction
import apod.about.vm.BuildState
import apod.core.ui.BackgroundSurface
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview

@Composable
internal fun AboutScreenImpl(
  buildState: BuildState,
  onAction: (AboutAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val theme = LocalTheme.current
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = { AboutTopBar(theme, scrollBehavior, onAction) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      AboutScreenContent(
        modifier = Modifier.padding(innerPadding),
        buildState = buildState,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun AboutScreenContent(
  buildState: BuildState,
  onAction: (AboutAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .background(theme.pageBackground)
      .fillMaxSize()
      .padding(15.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    AboutHeader(
      modifier = Modifier
        .wrapContentWidth()
        .padding(vertical = 5.dp),
      year = buildState.year,
      theme = theme,
    )

    AboutBuildState(
      modifier = Modifier.fillMaxWidth(),
      buildState = buildState,
      onAction = onAction,
    )

    VerticalSpacer(weight = 1f)

    AboutButtons(
      modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(),
      onAction = onAction,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewAbout() = PreviewScreen {
  AboutScreenImpl(
    buildState = PreviewBuildState,
    onAction = {},
  )
}
