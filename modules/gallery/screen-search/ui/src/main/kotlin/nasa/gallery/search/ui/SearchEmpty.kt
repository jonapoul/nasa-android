package nasa.gallery.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.color.Theme
import nasa.core.ui.preview.PreviewScreen
import nasa.core.ui.preview.ScreenPreview

@Suppress("UNUSED_PARAMETER")
@Composable
internal fun SearchEmpty(
  onAction: (SearchAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = stringResource(id = R.string.search_empty_title),
      fontSize = 25.sp,
    )
  }
}

@ScreenPreview
@Composable
private fun PreviewEmpty() = PreviewScreen {
  SearchEmpty(
    onAction = {},
  )
}
