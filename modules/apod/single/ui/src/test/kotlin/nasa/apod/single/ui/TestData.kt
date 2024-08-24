package nasa.apod.single.ui

import kotlinx.datetime.LocalDate
import nasa.apod.model.ApodItem
import nasa.apod.model.ApodMediaType
import java.time.Month

internal val EXAMPLE_ITEM_1 = ApodItem(
  date = LocalDate(year = 2024, month = Month.APRIL, dayOfMonth = 29),
  title = "Slightly Beneath Saturn's Ring Plane",
  explanation = "Shortened explanation for easier testing",
  mediaType = ApodMediaType.Image,
  copyright = null,
  url = "https://apod.nasa.gov/apod/image/0604/rhearings_cassini.jpg",
  hdUrl = "https://apod.nasa.gov/apod/image/0604/rhearings_cassini_big.jpg",
  thumbnailUrl = null,
)

internal val EXAMPLE_ITEM_2 = ApodItem(
  title = "GK Per: Nova and Planetary Nebula",
  date = LocalDate(year = 2024, month = Month.APRIL, dayOfMonth = 30),
  mediaType = ApodMediaType.Video,
  thumbnailUrl = "https://apod.nasa.gov/apod/image/2404/GKPerWide_DSC_960.jpg",
  copyright = "Deep Sky Collective",
  url = "https://youtube.com/some-url",
  hdUrl = null,
  explanation = "Here's a dummy explanation",
)
