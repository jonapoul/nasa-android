package nasa.licenses.ui

import alakazam.android.ui.compose.VerticalSpacer
import alakazam.kotlin.core.exhaustive
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import my.nanihadesuka.compose.LazyColumnScrollbar
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.scrollbarSettings
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.licenses.data.LibraryModel
import nasa.licenses.res.R

@Composable
internal fun LicensesScreenImpl(
  state: LicensesState,
  onAction: (LicensesAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { LicensesTopBar(theme, onAction) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      LicensesScreenContent(
        modifier = Modifier.padding(innerPadding),
        state = state,
        theme = theme,
        onAction = onAction,
      )
    }
  }
}

@Composable
private fun LicensesScreenContent(
  state: LicensesState,
  onAction: (LicensesAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  when (state) {
    LicensesState.Loading -> LoadingContent(theme, modifier)
    LicensesState.NoneFound -> NoneFoundContent(theme, modifier)
    is LicensesState.Loaded -> LoadedContent(theme, state.libraries, onAction, modifier)
    is LicensesState.Error -> ErrorContent(theme, state.errorMessage, onAction, modifier)
  }.exhaustive
}

@Composable
private fun LoadingContent(
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator(
      color = theme.pageTextPrimary,
      trackColor = theme.dialogProgressWheelTrack,
    )
  }
}

@Composable
private fun NoneFoundContent(
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Filled.Warning,
      contentDescription = null,
      tint = theme.warningText,
    )

    VerticalSpacer(16.dp)

    Text(
      text = stringResource(id = R.string.licenses_none_found),
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
      color = theme.warningText,
    )
  }
}

@Composable
private fun LoadedContent(
  theme: Theme,
  libraries: ImmutableList<LibraryModel>,
  onAction: (LicensesAction) -> Unit,
  modifier: Modifier = Modifier,
) {
  val listState = rememberLazyListState()
  LazyColumnScrollbar(
    modifier = modifier
      .fillMaxSize()
      .padding(8.dp),
    state = listState,
    settings = theme.scrollbarSettings(),
  ) {
    LazyColumn(
      state = listState,
    ) {
      items(libraries) { library ->
        LibraryItem(
          library = library,
          onLaunchUrl = { onAction(LicensesAction.LaunchUrl(it)) },
          theme = theme,
        )
      }
    }
  }
}

@Composable
private fun ErrorContent(
  theme: Theme,
  errorMessage: String,
  onAction: (LicensesAction) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(32.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Filled.Error,
      contentDescription = null,
      tint = theme.errorText,
    )

    VerticalSpacer(16.dp)

    Text(
      text = stringResource(id = R.string.licenses_failed, errorMessage),
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
      color = theme.errorText,
    )

    VerticalSpacer(16.dp)

    PrimaryTextButton(
      text = stringResource(id = R.string.licenses_failed_retry),
      theme = theme,
      onClick = { onAction(LicensesAction.Reload) },
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.Loading,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoneFound() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.NoneFound,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewLoaded() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.Loaded(
      libraries = persistentListOf(AlakazamAndroidCore, ComposeMaterialRipple, FragmentKtx, VoyagerScreenModel),
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewError() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.Error(errorMessage = "Something broke lol! Here's some more shite to show how it looks"),
    onAction = {},
  )
}
