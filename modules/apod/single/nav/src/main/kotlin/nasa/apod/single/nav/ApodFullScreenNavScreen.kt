package nasa.apod.single.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import nasa.apod.model.ApodItem

data class ApodFullScreenNavScreen(val item: ApodItem) : ScreenProvider
