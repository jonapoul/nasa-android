package nasa.licenses.ui

import alakazam.android.ui.compose.VerticalSpacer
import alakazam.kotlin.core.exhaustive
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import my.nanihadesuka.compose.LazyColumnScrollbar
import nasa.core.res.CoreDimens
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.color.scrollbarSettings
import nasa.core.ui.color.textField
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.text.NasaTextField
import nasa.core.ui.text.keyboardFocusRequester
import nasa.licenses.data.LibraryModel
import nasa.licenses.res.LicensesPlurals
import nasa.licenses.res.LicensesStrings
import nasa.licenses.vm.LicensesState
import nasa.licenses.vm.SearchBarState

@Composable
internal fun LicensesScreenImpl(
  state: LicensesState,
  searchBarState: SearchBarState,
  onAction: (LicensesAction) -> Unit,
) {
  val theme = LocalTheme.current
  Scaffold(
    topBar = { LicensesTopBar(searchBarState, theme, onAction) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      Column(
        modifier = Modifier.padding(innerPadding),
      ) {
        LicensesSearchInput(
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
          searchState = searchBarState,
          licensesState = state,
          onAction = onAction,
          theme = theme,
        )

        LicensesScreenContent(
          state = state,
          theme = theme,
          onAction = onAction,
        )
      }
    }
  }
}

@Composable
private fun LicensesSearchInput(
  searchState: SearchBarState,
  licensesState: LicensesState,
  onAction: (LicensesAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val keyboard = LocalSoftwareKeyboardController.current

  val isVisible = searchState is SearchBarState.Visible
  LaunchedEffect(isVisible) { if (isVisible) keyboard?.show() else keyboard?.hide() }

  AnimatedVisibility(
    visible = searchState is SearchBarState.Visible,
    enter = slideInVertically() + fadeIn(),
    exit = slideOutVertically() + fadeOut(),
  ) {
    val text = (searchState as? SearchBarState.Visible)?.text.orEmpty()
    val colors = theme.textField(
      focusedContainer = theme.toolbarBackgroundSubdued,
      text = theme.toolbarTextSubdued,
      icon = theme.toolbarTextSubdued,
    )

    Column(
      modifier = modifier
        .fillMaxWidth()
        .background(theme.toolbarBackgroundSubdued),
      horizontalAlignment = Alignment.End,
    ) {
      NasaTextField(
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(keyboardFocusRequester(keyboard)),
        value = text,
        onValueChange = { query -> onAction(LicensesAction.EditSearchText(query)) },
        placeholderText = LicensesStrings.searchPlaceholder,
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
        clearable = true,
        theme = theme,
        colors = colors,
      )

      val numResults = remember(licensesState) {
        (licensesState as? LicensesState.Loaded)?.libraries?.size ?: 0
      }
      Text(
        modifier = Modifier
          .wrapContentWidth()
          .padding(horizontal = CoreDimens.large),
        text = LicensesPlurals.searchNumResults(numResults, numResults),
        fontSize = 12.sp,
        color = theme.toolbarTextSubdued,
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

    VerticalSpacer(CoreDimens.large)

    Text(
      text = LicensesStrings.noneFound,
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
      .padding(CoreDimens.large),
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

    VerticalSpacer(CoreDimens.large)

    Text(
      text = LicensesStrings.failed(errorMessage),
      fontSize = 20.sp,
      textAlign = TextAlign.Center,
      color = theme.errorText,
    )

    VerticalSpacer(CoreDimens.large)

    PrimaryTextButton(
      text = LicensesStrings.failedRetry,
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
    searchBarState = SearchBarState.Gone,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewNoneFound() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.NoneFound,
    searchBarState = SearchBarState.Gone,
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
    searchBarState = SearchBarState.Visible(text = "My wicked search query"),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewError() = PreviewScreen {
  LicensesScreenImpl(
    state = LicensesState.Error(errorMessage = "Something broke lol! Here's some more shite to show how it looks"),
    searchBarState = SearchBarState.Gone,
    onAction = {},
  )
}
