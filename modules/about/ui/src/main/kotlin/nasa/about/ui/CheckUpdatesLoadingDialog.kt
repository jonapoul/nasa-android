package nasa.about.ui

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import nasa.about.res.AboutStrings
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.dialog.DialogContent
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun CheckUpdatesLoadingDialog(
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    onDismissRequest = onCancel,
    content = {
      CheckUpdatesLoadingDialogContent(
        onCancel = onCancel,
        theme = theme,
      )
    },
  )
}

@Composable
private fun CheckUpdatesLoadingDialogContent(
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = null,
    content = {
      Row(
        modifier = Modifier.padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        CircularProgressIndicator(
          color = theme.pageTextPrimary,
          trackColor = theme.dialogProgressWheelTrack,
        )

        HorizontalSpacer(15.dp)

        Text(
          text = AboutStrings.checkingUpdatesLoading,
          color = theme.pageText,
        )
      }
    },
    buttons = {
      TextButton(onClick = onCancel) {
        Text(
          text = AboutStrings.checkingUpdatesCancel,
          color = theme.pageTextPrimary,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  CheckUpdatesLoadingDialogContent(
    onCancel = {},
  )
}
