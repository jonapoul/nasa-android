package nasa.gallery.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.res.GalleryStrings

@Composable
internal fun SearchEmpty(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = GalleryStrings.searchEmptyTitle,
      color = theme.pageText,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewEmpty() = PreviewScreen {
  SearchEmpty(
    // TBC
  )
}
