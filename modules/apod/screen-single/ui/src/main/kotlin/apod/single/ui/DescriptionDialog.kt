package apod.single.ui

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.dialog.DialogContent
import apod.core.ui.preview.PreviewColumn
import apod.single.res.R
import apod.data.model.ApodItem

@Composable
internal fun DescriptionDialog(
  item: ApodItem,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  BasicAlertDialog(
    modifier = modifier,
    onDismissRequest = onCancel,
    content = {
      DescriptionDialogContent(
        item = item,
        onCancel = onCancel,
        theme = theme,
      )
    },
  )
}

@Composable
private fun DescriptionDialogContent(
  item: ApodItem,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  DialogContent(
    modifier = modifier,
    theme = theme,
    title = item.title,
    content = {
      Text(
        modifier = Modifier
          .wrapContentHeight()
          .verticalScroll(rememberScrollState()),
        text = item.explanation,
        fontSize = 14.sp,
      )
    },
    buttons = {
      TextButton(onClick = onCancel) {
        Text(
          text = stringResource(id = R.string.apod_desc_dialog_ok),
          color = theme.pageTextPositive,
        )
      }
    },
  )
}

@Preview
@Composable
private fun PreviewContent() = PreviewColumn {
  DescriptionDialogContent(
    item = EXAMPLE_ITEM,
    onCancel = {},
  )
}
