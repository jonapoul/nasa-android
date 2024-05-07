package nasa.about.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun UpdateCheckFailedDialog(
  cause: String,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      UpdateCheckFailedDialogContent(
        cause = cause,
        onDismiss = onDismiss,
        theme = theme,
      )
    },
  )
}

@Composable
private fun UpdateCheckFailedDialogContent(
  cause: String,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = stringResource(id = R.string.about_check_failed_title),
    icon = Icons.Filled.Warning,
    titleColor = theme.errorText,
    content = { Text(cause) },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = stringResource(id = R.string.about_check_failed_ok),
          color = theme.errorText,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  UpdateCheckFailedDialogContent(
    cause = "Something broke lol. And here's some other rubbish to show how the text looks when wrapping lines",
    onDismiss = {},
  )
}
