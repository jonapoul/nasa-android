package apod.settings.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.dialog.DialogContent
import apod.core.ui.preview.PreviewColumn
import apod.settings.vm.FileSize
import apod.settings.vm.megabytes

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
    title = stringResource(R.string.settings_clear_cache_dialog_title),
    content = {
      Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.settings_clear_cache_dialog_message, totalSize.toString()),
      )
    },
    buttons = {
      TextButton(onClick = onCancel) {
        Text(
          text = stringResource(R.string.settings_clear_cache_dialog_cancel),
          color = theme.pageTextPrimary,
        )
      }
      TextButton(onClick = onConfirm) {
        Text(
          text = stringResource(R.string.settings_clear_cache_dialog_confirm),
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
