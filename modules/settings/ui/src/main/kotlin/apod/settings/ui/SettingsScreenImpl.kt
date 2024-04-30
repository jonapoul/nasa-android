package apod.settings.ui

import alakazam.android.prefs.core.PrefPair
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastMap
import apod.core.model.ThemeType
import apod.core.ui.BackgroundSurface
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.settings.keys.SettingsKeys
import apod.settings.res.R
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun SettingsScreenImpl(
  onClickBack: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val theme = LocalTheme.current
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = { SettingsTopBar(theme, scrollBehavior, onClickBack) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      SettingsScreenContent(
        modifier = Modifier.padding(innerPadding),
        theme = theme,
      )
    }
  }
}

@Composable
private fun SettingsScreenContent(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  val preferences = rememberSharedPreferences()

  Column(
    modifier = modifier
      .background(theme.pageBackground)
      .fillMaxWidth()
      .fillMaxHeight(),
    verticalArrangement = Arrangement.Top,
  ) {
    SettingsCategory(
      icon = Icons.Filled.FormatPaint,
      title = stringResource(id = R.string.settings_style_category),
      theme = theme,
    )

    ListPreference(
      pair = AppThemePair,
      title = stringResource(id = R.string.settings_theme_title),
      icon = Icons.Filled.ColorLens,
      entryValues = ThemeType.entries.fastMap { it.name }.toPersistentList(),
      entries = themeEntries(),
      theme = theme,
      preferences = preferences,
    )
  }
}

@Stable
@Composable
private fun themeEntries() = persistentListOf(
  stringResource(id = R.string.settings_theme_system),
  stringResource(id = R.string.settings_theme_light),
  stringResource(id = R.string.settings_theme_dark),
  stringResource(id = R.string.settings_theme_midnight),
)

private val AppThemePair = SettingsKeys.AppTheme.map { it.name }

private fun <T, R> PrefPair<T>.map(transform: (T) -> R): PrefPair<R> = PrefPair(key, default = transform(default))

@ScreenPreview
@Composable
private fun PreviewSettings() = PreviewScreen {
  SettingsScreenImpl(
    onClickBack = {},
  )
}
