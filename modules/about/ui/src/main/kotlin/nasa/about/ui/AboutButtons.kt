package nasa.about.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nasa.about.res.AboutStrings
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
      modifier = buttonModifier.testTag(Tags.CheckUpdatesButton),
      text = AboutStrings.checkUpdates,
      onClick = { onAction(AboutAction.CheckUpdates) },
    )
    RegularTextButton(
      modifier = buttonModifier.testTag(Tags.ReportButton),
      text = AboutStrings.reportIssues,
      onClick = { onAction(AboutAction.ReportIssue) },
    )
    RegularTextButton(
      modifier = buttonModifier.testTag(Tags.LicensesButton),
      text = AboutStrings.licenses,
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
