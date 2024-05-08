package nasa.home.ui

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Composable
internal fun HomeScreenImpl(
  onAction: (HomeAction) -> Unit,
  theme: Theme = LocalTheme.current,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = { HomeTopBar(onAction, theme, scrollBehavior) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      HomeScreenContent(
        modifier = Modifier.padding(innerPadding),
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Composable
private fun HomeScreenContent(
  onAction: (HomeAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .background(theme.pageBackground)
      .fillMaxSize()
      .padding(15.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    PrimaryTextButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(R.string.home_nav_apod),
      onClick = { onAction(HomeAction.NavApodToday) },
    )

    VerticalSpacer(20.dp)

    PrimaryTextButton(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(R.string.home_nav_gallery),
      onClick = { onAction(HomeAction.NavGallery) },
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewHome() = PreviewScreen {
  HomeScreenImpl(
    onAction = {},
  )
}
