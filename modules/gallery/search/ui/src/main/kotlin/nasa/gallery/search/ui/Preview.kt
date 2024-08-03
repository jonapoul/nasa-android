package nasa.gallery.search.ui

import kotlinx.collections.immutable.persistentListOf
import nasa.gallery.model.NasaId
import nasa.gallery.search.vm.SearchResultItem
import nasa.gallery.search.vm.SearchState

internal val EXAMPLE_ITEM_1 = SearchResultItem(
  nasaId = NasaId("abc"),
)

internal val EXAMPLE_ITEM_2 = SearchResultItem(
  nasaId = NasaId("xyz"),
)

internal val PreviewSuccessState = SearchState.Success(
  results = persistentListOf(EXAMPLE_ITEM_1, EXAMPLE_ITEM_2),
  totalResults = 123,
  prevPageNumber = null,
  pageNumber = 1,
  nextPageNumber = 2,
  resultsPerPage = 50,
)
