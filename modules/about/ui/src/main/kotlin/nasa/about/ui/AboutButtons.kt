package nasa.about.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.core.ui.button.RegularTextButton
import nasa.core.ui.preview.PreviewColumn

@Composable
internal fun AboutButtons(
  onAction: (AboutAction) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val buttonModifier = Modifier.fillMaxWidth()

    RegularTextButton(
      modifier = buttonModifier,
      text = stringResource(id = R.string.about_check_updates),
      onClick = { onAction(AboutAction.CheckUpdates) },
    )
    RegularTextButton(
      modifier = buttonModifier,
      text = stringResource(id = R.string.about_report_issues),
      onClick = { onAction(AboutAction.ReportIssue) },
    )
    RegularTextButton(
      modifier = buttonModifier,
      text = stringResource(id = R.string.about_licenses),
      onClick = { onAction(AboutAction.ViewLicenses) },
    )
  }
}

@Preview
@Composable
private fun PreviewAboutButtons() = PreviewColumn {
  AboutButtons(
    modifier = Modifier.width(300.dp),
    onAction = {},
  )
}
