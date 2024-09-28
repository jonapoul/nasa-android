package nasa.gallery.ui.search

import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import nasa.gallery.model.Album
import nasa.gallery.model.Center
import nasa.gallery.model.FilterConfig
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.gallery.model.Photographer
import nasa.gallery.vm.search.SearchResultItem
import nasa.gallery.vm.search.SearchState

// Mon May 06 2024 14:23:21
internal val PREVIEW_TIME = Instant.fromEpochMilliseconds(1715005401531L)

internal val PREVIEW_ITEM_1 = SearchResultItem(
  nasaId = NasaId(value = "abc"),
  collectionUrl = JsonUrl(url = "https://url.com/data.json"),
  previewUrl = ImageUrl(url = "https://url.com/data.jpg"),
  captionsUrl = ImageUrl(url = "https://url.com/data.src"),
  albums = persistentListOf(Album("Apollo")),
  center = Center(value = "ABCC"),
  title = "Hello World, this is a long title which should be ellipsized when it gets longer than 2 lines but here's" +
    "some more rubbish that should be clipped",
  keywords = Keywords("Apollo", "NASA", "Space"),
  location = "123 Road Street, Townsville",
  photographer = Photographer("Dick Dastardly"),
  dateCreated = PREVIEW_TIME,
  mediaType = MediaType.Image,
  secondaryCreator = "Willy Wonka",
  description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore" +
    " et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex " +
    "ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
    "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim " +
    "id est laborum.",
  description508 = null,
)

internal val PREVIEW_ITEM_2 = PREVIEW_ITEM_1.copy(
  nasaId = NasaId("xyz"),
  albums = null,
  title = "Here's another one",
  mediaType = MediaType.Video,
  captionsUrl = null,
  keywords = Keywords("Some", "Other", "Keywords"),
  photographer = null,
)

internal val PREVIEW_SUCCESS_STATE = SearchState.Success(
  results = persistentListOf(PREVIEW_ITEM_1, PREVIEW_ITEM_2),
  totalResults = 123,
  prevPageNumber = null,
  pageNumber = 1,
  nextPageNumber = 2,
  resultsPerPage = 50,
)

internal val PREVIEW_FILTER_CONFIG = FilterConfig(
  query = null,
  center = null,
  description = null,
  keywords = null,
  location = null,
  mediaTypes = null,
  nasaId = null,
  photographer = null,
  secondaryCreator = null,
  title = null,
  yearStart = null,
  yearEnd = null,
)
