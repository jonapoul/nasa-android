package apod.single.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import apod.core.res.R
import apod.core.ui.CardShape
import apod.core.ui.color.LocalTheme
import apod.core.ui.color.Theme
import apod.core.ui.preview.PreviewScreen
import apod.core.ui.preview.ScreenPreview
import apod.single.vm.ApodSingleAction
import apod.single.vm.LoadState

@Composable
internal fun ItemContent(
  state: LoadState,
  onAction: (ApodSingleAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(theme.cardBackground, CardShape)
      .padding(horizontal = 16.dp)
      .clickable {
        if (state is LoadState.Success) {
          onAction(ApodSingleAction.ShowImageFullscreen(state.item))
        }
      },
    contentAlignment = Alignment.Center,
  ) {
    if (isLoading) {
      CircularProgressIndicator(
        color = theme.pageTextPositive,
        trackColor = theme.dialogProgressWheelTrack,
      )
    } else {
      Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.mipmap.app_icon_round), // TODO: replace
        contentDescription = null,
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
      )
    }
  }
}

@ScreenPreview
@Composable
private fun PreviewLoading() = PreviewScreen {
  ItemContent(
    item = EXAMPLE_ITEM,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun PreviewRegular() = PreviewScreen {
  ItemContent(
    item = EXAMPLE_ITEM,
    onAction = {},
  )
}
