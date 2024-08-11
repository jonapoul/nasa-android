package nasa.gallery.search.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.NasaId
import nasa.gallery.model.Photographer
import nasa.gallery.model.Year

internal class MutableFilterConfig(config: FilterConfig) {
  var query by mutableStateOf<String?>(config.query)
  var center by mutableStateOf<Center?>(config.center)
  var description by mutableStateOf<String?>(config.description)
  var keywords by mutableStateOf<Keywords?>(config.keywords)
  var location by mutableStateOf<String?>(config.location)
  var mediaTypes by mutableStateOf<MediaTypes?>(config.mediaTypes)
  var nasaId by mutableStateOf<NasaId?>(config.nasaId)
  var photographer by mutableStateOf<Photographer?>(config.photographer)
  var secondaryCreator by mutableStateOf<String?>(config.secondaryCreator)
  var title by mutableStateOf<String?>(config.title)
  var yearStart by mutableStateOf<Year>(config.yearStart ?: Year.Minimum)
  var yearEnd by mutableStateOf<Year>(config.yearEnd ?: Year.Maximum)
}
