package nasa.about.ui

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.about.res.AboutStrings
import nasa.core.res.CoreStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.core.res.R as CoreR

@Composable
internal fun AboutHeader(
  year: Int,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .wrapContentWidth()
      .padding(10.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val appName = CoreStrings.appName
    Image(
      modifier = Modifier.size(80.dp),
      painter = painterResource(id = CoreR.mipmap.app_icon_round),
      contentDescription = appName,
    )

    HorizontalSpacer(10.dp)

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = appName,
        fontSize = 30.sp,
        fontWeight = FontWeight.W700,
        color = theme.pageText,
      )
      Text(
        text = AboutStrings.subtitle1(year),
        color = theme.pageTextSubdued,
      )
      Text(
        text = AboutStrings.subtitle2,
        color = theme.pageTextSubdued,
      )
    }
  }
}

@Preview
@Composable
private fun PreviewHeader() = PreviewColumn {
  AboutHeader(year = 2024)
}
