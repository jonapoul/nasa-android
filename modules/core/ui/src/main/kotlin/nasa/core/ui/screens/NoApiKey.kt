package nasa.core.ui.screens

import alakazam.android.ui.compose.HorizontalSpacer
import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.res.CoreStrings
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.MY_PHONE_HEIGHT_DP
import nasa.core.ui.preview.MY_PHONE_WIDTH_DP
import nasa.core.ui.preview.PreviewColumn

@Composable
fun NoApiKey(
  onClickRegister: () -> Unit,
  onClickSettings: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .wrapContentSize()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(
      modifier = Modifier.size(70.dp),
      imageVector = Icons.Filled.Key,
      contentDescription = null,
      tint = theme.warningText,
    )

    VerticalSpacer(10.dp)

    Text(
      text = CoreStrings.noKeyTitle,
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      color = theme.warningText,
      fontSize = 25.sp,
    )

    VerticalSpacer(20.dp)

    Text(
      text = CoreStrings.noKeyMessage,
      textAlign = TextAlign.Center,
      color = theme.warningText,
    )

    VerticalSpacer(20.dp)

    Row {
      PrimaryTextButton(
        text = CoreStrings.noKeyRegister,
        onClick = onClickRegister,
        theme = theme,
      )

      HorizontalSpacer(20.dp)

      PrimaryTextButton(
        text = CoreStrings.noKeySettings,
        onClick = onClickSettings,
        theme = theme,
      )
    }
  }
}

@Preview(
  widthDp = MY_PHONE_WIDTH_DP,
  heightDp = (MY_PHONE_HEIGHT_DP * 1.2f).toInt(),
)
@Composable
private fun PreviewNoApiKey() = PreviewColumn {
  NoApiKey(
    onClickRegister = {},
    onClickSettings = {},
  )
}
