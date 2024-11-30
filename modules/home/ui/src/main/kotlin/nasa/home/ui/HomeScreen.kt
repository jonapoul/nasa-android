package nasa.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import nasa.about.nav.AboutNavScreen
import nasa.apod.nav.ApodSingleTodayNavScreen
import nasa.core.ui.color.LocalTheme
import nasa.core.ui.set
import nasa.gallery.nav.GallerySearchNavScreen
import nasa.home.vm.HomeViewModel
import nasa.settings.nav.SettingsNavScreen

@Composable
fun HomeScreen(
  navController: NavController,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val theme = LocalTheme.current

  val apodThumbnailUrl by viewModel.apodThumbnailUrl.collectAsState()
  val galleryThumbnailUrl by viewModel.galleryThumbnailUrl.collectAsState()

  val showApiUsageDialog = remember { mutableStateOf(false) }
  if (showApiUsageDialog.value) {
    val state by viewModel.apiUsage().collectAsState()
    ApiUsageDialog(
      state = state,
      theme = theme,
      onDismiss = { showApiUsageDialog.set(false) },
      onClickRegister = { viewModel.registerForApiKey() },
    )
  }

  HomeScreenImpl(
    thumbnailUrls = ThumbnailUrls(apodThumbnailUrl, galleryThumbnailUrl),
    theme = theme,
    onAction = { action ->
      when (action) {
        HomeAction.NavAbout -> navController.navigate(route = AboutNavScreen)
        HomeAction.NavSettings -> navController.navigate(route = SettingsNavScreen)
        HomeAction.NavApodToday -> navController.navigate(route = ApodSingleTodayNavScreen)
        HomeAction.NavGallery -> navController.navigate(route = GallerySearchNavScreen)
        HomeAction.ShowApiUsage -> showApiUsageDialog.set(true)
      }
    },
  )
}
