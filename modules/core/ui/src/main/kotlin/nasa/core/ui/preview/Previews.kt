package nasa.core.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

/**
 * Previews three portrait phone screens in a row. Make sure to use the [ScreenPreview] annotation in conjunction with
 * the [PreviewScreen] composable below. Poor naming, I know
 */
@Preview(
  name = "Screen",
  showBackground = true,
  uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED,
  widthDp = 3 * MY_PHONE_WIDTH_DP,
  heightDp = MY_PHONE_HEIGHT_DP,
)
annotation class ScreenPreview

@Composable
fun PreviewScreen(
  modifier: Modifier = Modifier,
  previewModifier: Modifier = Modifier
    .width(MY_PHONE_WIDTH_DP.dp)
    .height(MY_PHONE_HEIGHT_DP.dp),
  content: @Composable () -> Unit,
) {
  Row(modifier = modifier) {
    ThemesToPreview.fastForEach { theme ->
      PreviewWithTheme(
        modifier = previewModifier,
        theme = theme,
        content = content,
      )
    }
  }
}
