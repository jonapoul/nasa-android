package nasa.gallery.model

fun filterConfig(
  query: String = "",
  yearStart: Year = Year.Minimum,
  yearEnd: Year = Year.Maximum,
  mediaTypes: MediaTypes? = null,
): FilterConfig = FilterConfig(
  query = query,
  yearStart = yearStart,
  yearEnd = yearEnd,
  mediaTypes = mediaTypes,
  center = null,
  description = null,
  keywords = null,
  location = null,
  nasaId = null,
  photographer = null,
  secondaryCreator = null,
  title = null,
)
