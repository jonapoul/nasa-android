package nasa.about.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import nasa.about.res.AboutStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun UpdateFoundDialog(
  currentVersion: String,
  latestVersion: String,
  latestUrl: String,
  onDismiss: () -> Unit,
  onOpenUrl: (String) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onDismiss,
    content = {
      UpdateFoundDialogContent(
        currentVersion = currentVersion,
        latestVersion = latestVersion,
        latestUrl = latestUrl,
        onDismiss = onDismiss,
        onOpenUrl = onOpenUrl,
        theme = theme,
      )
    },
  )
}

@Composable
private fun UpdateFoundDialogContent(
  currentVersion: String,
  latestVersion: String,
  latestUrl: String,
  onOpenUrl: (String) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = AboutStrings.updateFoundTitle,
    titleColor = theme.successText,
    content = {
      TextContent(AboutStrings.updateFoundInstalled, currentVersion)
      TextContent(AboutStrings.updateFoundLatest, latestVersion)
    },
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = AboutStrings.updateFoundDismiss,
          color = theme.successText,
        )
      }
      TextButton(onClick = { onOpenUrl(latestUrl) }) {
        Text(
          text = AboutStrings.updateFoundView,
          color = theme.successText,
        )
      }
    },
  )
}

@Composable
private fun TextContent(
  text: String,
  value: String,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
  ) {
    Text(text, modifier = Modifier.weight(1f), textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)
    Text(value, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
  }
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  UpdateFoundDialogContent(
    currentVersion = "v1.2.3",
    latestVersion = "v2.3.4",
    latestUrl = "https://github.com/jonapoul/nasa-android/releases/v2.3.4",
    onOpenUrl = {},
    onDismiss = {},
  )
}
