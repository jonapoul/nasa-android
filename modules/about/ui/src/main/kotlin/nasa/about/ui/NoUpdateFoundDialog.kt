package nasa.about.ui

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nasa.about.res.AboutStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn

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
    title = AboutStrings.noUpdateTitle,
    content = {
      Text(text = AboutStrings.noUpdateMessage)
    },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = AboutStrings.noUpdateOk,
          color = theme.pageTextPrimary,
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
