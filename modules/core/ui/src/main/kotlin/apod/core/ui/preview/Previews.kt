package apod.core.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import apod.core.model.ThemeType
import apod.core.ui.ApodTheme

/**
 * Previews the given [content] three times in a vertical column
 */
@Composable
fun PreviewColumn(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  Column(modifier = modifier) {
    ThemesToPreview.fastForEach { theme ->
      Box {
        PreviewWithTheme(
          theme = theme,
          content = content,
        )
      }
    }
  }
}

@Composable
internal fun PreviewWithTheme(
  theme: ThemeType,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
) {
  ApodTheme(theme) {
    Surface(modifier = modifier) {
      content()
    }
  }
}
