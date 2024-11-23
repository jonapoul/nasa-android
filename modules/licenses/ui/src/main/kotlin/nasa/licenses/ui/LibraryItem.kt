package nasa.licenses.ui

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import nasa.core.res.CoreDimens
import nasa.core.ui.CardShape
import nasa.core.ui.button.RegularIconButton
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewColumn
import nasa.licenses.data.LibraryModel
import nasa.licenses.res.LicensesStrings

@Composable
internal fun LibraryItem(
  library: LibraryModel,
  onLaunchUrl: (url: String) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .shadow(CoreDimens.medium)
      .padding(CoreDimens.small)
      .background(theme.cardBackground, CardShape)
      .padding(CoreDimens.huge),
    verticalAlignment = Alignment.Top,
  ) {
    Column(
      modifier = Modifier.weight(1f),
    ) {
      Text(
        text = library.project,
        fontWeight = FontWeight.W700,
        color = theme.pageTextPrimary,
        fontSize = 15.sp,
      )

      LibraryTableRow(title = LicensesStrings.itemAuthors, value = library.developers.joinToString())
      LibraryTableRow(title = LicensesStrings.itemArtifact, value = library.dependency)
      LibraryTableRow(title = LicensesStrings.itemVersion, value = library.version)
      LibraryTableRow(title = LicensesStrings.itemYear, value = library.year?.toString())
      LibraryTableRow(title = LicensesStrings.itemLicense, value = library.licenses.firstOrNull()?.license)
      LibraryTableRow(title = LicensesStrings.itemDescription, value = library.description)
    }

    val url = library.url
    if (url != null) {
      RegularIconButton(
        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
        contentDescription = LicensesStrings.itemLaunch,
        onClick = { onLaunchUrl.invoke(url) },
      )
    }
  }
}

@Suppress("MagicNumber")
@Stable
@Composable
private fun LibraryTableRow(
  title: String,
  value: String?,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  if (value.isNullOrEmpty()) {
    // Nothing to show
    return
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    verticalAlignment = Alignment.Top,
  ) {
    Text(
      modifier = Modifier
        .weight(1f)
        .wrapContentHeight(),
      text = title,
      textAlign = TextAlign.Start,
      color = theme.pageTextSubdued,
      lineHeight = LineHeight,
      fontSize = TextSize,
      fontWeight = FontWeight.Bold,
    )

    HorizontalSpacer(CoreDimens.medium)

    Text(
      modifier = Modifier
        .weight(3f)
        .wrapContentHeight(),
      text = value,
      textAlign = TextAlign.Start,
      color = theme.pageText,
      lineHeight = LineHeight,
      fontSize = TextSize,
    )
  }
}

private val LineHeight = 15.sp
private val TextSize = 12.sp

@Preview
@Composable
private fun PreviewItem() = PreviewColumn {
  LibraryItem(
    library = AlakazamAndroidCore,
    onLaunchUrl = {},
  )
}
