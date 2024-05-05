package apod.licenses.ui

import alakazam.android.ui.compose.HorizontalSpacer
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apod.core.ui.CardShape
import apod.core.ui.button.RegularIconButton
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewColumn
import apod.licenses.data.LibraryModel
import apod.licenses.res.R

@Composable
internal fun LibraryItem(
  library: LibraryModel,
  onLaunchUrl: (url: String) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .shadow(4.dp)
      .padding(2.dp)
      .background(theme.cardBackground, CardShape)
      .padding(16.dp),
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

      LibraryTableRow(title = R.string.licenses_item_authors, value = library.developers.joinToString())
      LibraryTableRow(title = R.string.licenses_item_artifact, value = library.dependency)
      LibraryTableRow(title = R.string.licenses_item_version, value = library.version)
      LibraryTableRow(title = R.string.licenses_item_license, value = library.licenses.firstOrNull()?.license)
      LibraryTableRow(title = R.string.licenses_item_description, value = library.description)
    }

    val url = library.url
    if (url != null) {
      RegularIconButton(
        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
        contentDescription = stringResource(id = R.string.licenses_item_launch),
        onClick = { onLaunchUrl.invoke(url) },
      )
    }
  }
}

@Suppress("MagicNumber")
@Stable
@Composable
private fun LibraryTableRow(
  @StringRes title: Int,
  value: String?,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  if (value == null) {
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
      text = stringResource(id = title),
      textAlign = TextAlign.Start,
      color = theme.pageTextSubdued,
      lineHeight = LineHeight,
      fontSize = TextSize,
      fontWeight = FontWeight.Bold,
    )

    HorizontalSpacer(4.dp)

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
