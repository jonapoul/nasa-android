package apod.about.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import apod.about.res.R
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.dialog.DialogContent
import apod.core.ui.preview.PreviewColumn

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
          color = theme.pageTextPositive,
          trackColor = theme.dialogProgressWheelTrack,
        )

        HorizontalSpacer(15.dp)

        Text(
          text = stringResource(id = R.string.about_checking_updates_loading),
          color = theme.pageText,
        )
      }
    },
    buttons = {
      TextButton(onClick = onCancel) {
        Text(
          text = stringResource(id = R.string.about_checking_updates_cancel),
          color = theme.pageTextPositive,
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
