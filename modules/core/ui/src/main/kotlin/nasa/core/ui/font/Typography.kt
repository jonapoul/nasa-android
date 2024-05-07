package nasa.core.ui.font

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.core.ui.R as CoreR

@Composable
@ReadOnlyComposable
fun nasaTypography(theme: Theme = LocalTheme.current): Typography {
  /**
   * WARNING: DON'T SET A COLOUR FOR bodyLarge - IT'LL OVERRIDE THOSE SET IN TEXT FIELDS
   */
  return Typography(
    displayLarge = NasaFontFamily.style(FontWeight.W700, fontSize = 30.sp),
    displayMedium = NasaFontFamily.style(FontWeight.W600, fontSize = 25.sp),
    displaySmall = NasaFontFamily.style(FontWeight.W500, fontSize = 20.sp),
    headlineLarge = NasaFontFamily.style(FontWeight.W700, theme.pageTextPrimary, fontSize = 30.sp),
    headlineMedium = NasaFontFamily.style(FontWeight.W600, theme.pageText, fontSize = 25.sp),
//     headlineSmall =
//     titleLarge =
//     titleMedium =
//     titleSmall =
    bodyLarge = NasaFontFamily.style(fontSize = 16.sp, lineHeight = 22.4.sp),
//     bodyMedium =
//     bodySmall =
//     labelLarge =
    labelMedium = NasaFontFamily.style(color = theme.pageTextSubdued, fontSize = 13.sp),
//     labelSmall =
  )
}

val NasaFontFamily = FontFamily(
  Font(CoreR.font.ptsans_regular, FontWeight.W400),
  Font(CoreR.font.ptsans_bold, FontWeight.W700),
)

fun FontFamily.style(
  fontWeight: FontWeight? = null,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  lineHeight: TextUnit = TextUnit.Unspecified,
): TextStyle = TextStyle(
  fontSize = fontSize,
  fontWeight = fontWeight,
  fontFamily = this,
  color = color,
  lineHeight = lineHeight,
)

@ScreenPreview
@Composable
private fun PreviewTypography() = PreviewScreen {
  val styles = with(nasaTypography()) {
    listOf(
      displayLarge,
      displayMedium,
      displaySmall,
      headlineLarge,
      headlineMedium,
//         headlineSmall,
//         titleLarge,
//         titleMedium,
//         titleSmall,
      bodyLarge,
//         bodyMedium,
//         bodySmall,
//         labelLarge,
      labelMedium,
//         labelSmall,
    )
  }
  Column {
    styles.fastForEach { style ->
      Text(
        modifier = Modifier.padding(8.dp),
        text = "Quick brown fox? Jumped over the lazy dog!",
        style = style,
      )
    }
  }
}
