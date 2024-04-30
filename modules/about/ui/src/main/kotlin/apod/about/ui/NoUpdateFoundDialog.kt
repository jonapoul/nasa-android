package apod.about.ui

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import apod.about.res.R
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.dialog.DialogContent
import apod.core.ui.preview.PreviewColumn

@Composable
internal fun NoUpdateFoundDialog(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      NoUpdateFoundDialogContent(
        onDismiss = onDismiss,
        theme = theme,
      )
    },
  )
}

@Composable
private fun NoUpdateFoundDialogContent(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = stringResource(id = R.string.about_no_update_title),
    content = {
      Text(text = stringResource(id = R.string.about_no_update_message))
    },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(id = R.string.about_no_update_ok),
          color = theme.pageTextPositive,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  NoUpdateFoundDialogContent(
    onDismiss = {},
  )
}
