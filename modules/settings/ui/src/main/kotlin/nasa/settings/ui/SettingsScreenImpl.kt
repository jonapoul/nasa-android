package nasa.settings.ui

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import nasa.core.model.FileSize
import nasa.core.model.SettingsKeys
import nasa.core.model.ThemeType
import nasa.core.model.kilobytes
import nasa.core.model.megabytes
import nasa.core.ui.BackgroundSurface
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.settings.res.SettingsStrings

@Composable
internal fun SettingsScreenImpl(
  imagesSize: FileSize,
  databaseSize: FileSize,
  onAction: (SettingsAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val theme = LocalTheme.current
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = { SettingsTopBar(theme, scrollBehavior, onAction) },
  ) { innerPadding ->
    BackgroundSurface(theme = theme) {
      SettingsScreenContent(
        modifier = Modifier.padding(innerPadding),
        imagesSize = imagesSize,
        databaseSize = databaseSize,
        theme = theme,
        onAction = onAction,
      )
    }
  }
}

@Composable
private fun SettingsScreenContent(
  imagesSize: FileSize,
  databaseSize: FileSize,
  onAction: (SettingsAction) -> Unit,
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
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    SettingsCategory(
      icon = Icons.Filled.FormatPaint,
      title = SettingsStrings.styleCategory,
      theme = theme,
    )

    ListPreference(
      key = SettingsKeys.APP_THEME,
      defaultValue = ThemeType.System?.toString(),
      title = SettingsStrings.themeTitle,
      icon = Icons.Filled.ColorLens,
      entryValues = ThemeType.entries.fastMap { it.name }.toPersistentList(),
      entries = themeEntries(),
      theme = theme,
      preferences = preferences,
    )

    VerticalSpacer(20.dp)

    SettingsCategory(
      icon = Icons.Filled.Lock,
      title = SettingsStrings.authCategory,
      theme = theme,
    )

    ApiKeyPreference(
      theme = theme,
      preferences = preferences,
    )

    PrimaryTextButton(
      modifier = Modifier.wrapContentWidth(),
      text = SettingsStrings.keyButton,
      theme = theme,
      onClick = { onAction(SettingsAction.RegisterForKey) },
    )

    VerticalSpacer(20.dp)

    SettingsCategory(
      icon = Icons.Filled.Storage,
      title = SettingsStrings.clearCacheTitle,
      theme = theme,
    )

    CachedImagesText(
      modifier = Modifier.fillMaxWidth(),
      images = imagesSize,
      database = databaseSize,
      theme = theme,
    )

    VerticalSpacer(10.dp)

    PrimaryTextButton(
      modifier = Modifier.wrapContentWidth(),
      text = SettingsStrings.clearCacheButton,
      theme = theme,
      onClick = { onAction(SettingsAction.ClearCache) },
    )
  }
}

@Stable
@Composable
private fun themeEntries() = persistentListOf(
  SettingsStrings.themeSystem,
  SettingsStrings.themeLight,
  SettingsStrings.themeDark,
  SettingsStrings.themeMidnight,
)

@Composable
private fun CachedImagesText(
  images: FileSize,
  database: FileSize,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier,
  ) {
    persistentMapOf(
      images to SettingsStrings.clearCacheImages,
      database to SettingsStrings.clearCacheDb,
    ).forEach { (size, text) ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          modifier = Modifier.weight(1f),
          text = text,
          textAlign = TextAlign.End,
          color = theme.pageTextSubdued,
        )
        HorizontalSpacer(30.dp)
        Text(
          modifier = Modifier.weight(1f),
          text = size.toString(),
          textAlign = TextAlign.Start,
          color = theme.pageText,
        )
      }
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewSettings() = PreviewScreen {
  SettingsScreenImpl(
    imagesSize = 234.megabytes,
    databaseSize = 456.kilobytes,
    onAction = {},
  )
}
