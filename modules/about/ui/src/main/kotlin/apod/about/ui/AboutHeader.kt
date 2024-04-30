package apod.about.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.about.res.R
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn
import apod.core.res.R as CoreR

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
    val appName = stringResource(id = CoreR.string.app_name)
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
        text = stringResource(id = R.string.about_subtitle1, year),
        color = theme.pageTextSubdued,
      )
      Text(
        text = stringResource(id = R.string.about_subtitle2),
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
