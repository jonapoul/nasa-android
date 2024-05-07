package nasa.core.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import nasa.core.model.ThemeType
import nasa.core.ui.NasaTheme

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
  NasaTheme(theme) {
    Surface(modifier = modifier) {
      content()
    }
  }
}
