package nasa.settings.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import nasa.core.model.FileSize
import nasa.core.model.megabytes
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn
import nasa.settings.res.R

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
