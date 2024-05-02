package apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import apod.core.model.ApodItem
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class ApodFullScreen(
  val item: ApodItem,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.pop()
      clickedBack = false
    }

    ApodFullScreenImpl(
      item = item,
      onClickedBack = { clickedBack = true },
    )
  }
}
