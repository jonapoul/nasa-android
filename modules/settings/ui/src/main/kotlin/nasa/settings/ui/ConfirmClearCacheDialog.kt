package nasa.settings.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nasa.core.model.FileSize
import nasa.core.model.megabytes
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn
import nasa.settings.res.SettingsStrings

@Composable
internal fun ConfirmClearCacheDialog(
  totalSize: FileSize,
  onConfirm: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onCancel,
    content = {
      ConfirmClearCacheDialogContent(
        totalSize = totalSize,
        onConfirm = onConfirm,
        onCancel = onCancel,
        theme = theme,
      )
    },
  )
}

@Composable
private fun ConfirmClearCacheDialogContent(
  totalSize: FileSize,
  onConfirm: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = SettingsStrings.clearCacheDialogTitle,
    content = {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = SettingsStrings.clearCacheDialogMessage(totalSize.toString()),
      )
    },
    buttons = {
      TextButton(onClick = onCancel) {
        Text(
          text = SettingsStrings.clearCacheDialogCancel,
          color = theme.pageTextPrimary,
        )
      }
      TextButton(onClick = onConfirm) {
        Text(
          text = SettingsStrings.clearCacheDialogConfirm,
          color = theme.pageTextPrimary,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  ConfirmClearCacheDialogContent(
    totalSize = 123.megabytes,
    onConfirm = {},
    onCancel = {},
  )
}
