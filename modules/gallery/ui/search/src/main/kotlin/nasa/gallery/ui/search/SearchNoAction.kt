package nasa.gallery.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview
import nasa.gallery.res.GalleryStrings

@Composable
internal fun SearchNoAction(
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(
      modifier = Modifier.size(70.dp),
      imageVector = Icons.Filled.Keyboard,
      contentDescription = null,
    )

    Text(
      text = GalleryStrings.searchNoAction,
      color = theme.pageText,
      fontSize = 18.sp,
      textAlign = TextAlign.Center,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewNoAction() = PreviewScreen {
  SearchNoAction(
    // TBC
  )
}
