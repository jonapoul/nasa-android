package nasa.apod.ui.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.datetime.LocalDate

@Composable
fun ApodFullScreen(
  date: LocalDate,
  navController: NavController,
  viewModel: ApodFullScreenViewModel = hiltViewModel(),
) {
  val progress by viewModel.downloadProgress.collectAsStateWithLifecycle()
  val item by viewModel.item.collectAsStateWithLifecycle()

  LaunchedEffect(date) { viewModel.loadDate(date) }

  ApodFullScreenImpl(
    date = date,
    item = item,
    progress = progress,
    onClickedBack = { navController.popBackStack() },
  )
}
