package nasa.gallery.ui.search

import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.res.R

@Composable
internal fun SearchSearching(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  LoadingImpl(
    modifier = modifier,
    text = stringResource(id = R.string.search_searching),
    theme = theme,
  )
}

@Composable
internal fun SearchLoadingPage(
  pageNumber: Int,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  LoadingImpl(
    modifier = modifier,
    text = stringResource(id = R.string.search_loading_page, pageNumber),
    theme = theme,
  )
}

@Composable
private fun LoadingImpl(
  text: String,
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    CircularProgressIndicator(
      color = theme.pageTextPrimary,
      trackColor = theme.dialogProgressWheelTrack,
    )

    VerticalSpacer(20.dp)

    Text(
      text = text,
      fontSize = 18.sp,
      color = theme.pageText,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewSearching() = PreviewScreen {
  SearchSearching(
    // TBC
  )
}

@ScreenPreview
@Composable
private fun PreviewLoadingPage() = PreviewScreen {
  SearchLoadingPage(
    pageNumber = 2,
  )
}
