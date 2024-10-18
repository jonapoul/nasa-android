package nasa.core.ui.screens

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.res.R
import nasa.core.ui.Dimensions
import nasa.core.ui.button.PrimaryTextButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn

@Composable
fun LoadFailure(
  message: String,
  onRetryLoad: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .wrapContentSize()
      .padding(Dimensions.Large),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(
      modifier = Modifier.size(70.dp),
      imageVector = Icons.Filled.Warning,
      contentDescription = null,
      tint = theme.errorText,
    )

    VerticalSpacer(10.dp)

    Text(
      text = stringResource(id = R.string.failed_title),
      textAlign = TextAlign.Center,
      fontWeight = FontWeight.Bold,
      color = theme.errorText,
      fontSize = 25.sp,
    )

    VerticalSpacer(10.dp)

    Text(
      text = message,
      textAlign = TextAlign.Center,
    )

    VerticalSpacer(20.dp)

    PrimaryTextButton(
      text = stringResource(id = R.string.failed_retry),
      onClick = onRetryLoad,
      theme = theme,
    )
  }
}

@Preview
@Composable
private fun PreviewLoadFailure() = PreviewColumn {
  LoadFailure(
    message = "This is an error message",
    onRetryLoad = {},
  )
}
