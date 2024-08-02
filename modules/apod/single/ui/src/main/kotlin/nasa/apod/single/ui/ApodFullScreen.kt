package nasa.apod.single.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import nasa.apod.model.ApodItem
import nasa.apod.single.vm.ApodFullScreenViewModel
import nasa.core.ui.getViewModel

data class ApodFullScreen(
  val item: ApodItem,
) : Screen {
  @Composable
  override fun Content() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = getViewModel<ApodFullScreenViewModel>()
    val progress by viewModel.downloadProgress.collectAsStateWithLifecycle()

    var clickedBack by remember { mutableStateOf(false) }
    if (clickedBack) {
      navigator.pop()
      clickedBack = false
    }

    ApodFullScreenImpl(
      item = item,
      progress = progress,
      onClickedBack = { clickedBack = true },
    )
  }
}
