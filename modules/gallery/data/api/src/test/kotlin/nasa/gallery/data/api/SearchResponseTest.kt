package nasa.gallery.data.api

import alakazam.test.core.getResourceAsStream
import kotlinx.datetime.Instant
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import org.junit.Test
import kotlin.test.assertEquals

class SearchResponseTest {
  @Test
  fun `Deserialize failure`() {
    val json = getJson(filename = "search-failure.json")
    val response = GalleryJson.decodeFromString<SearchResponse>(json)
    assertEquals(
      expected = SearchResponse.Failure(reason = "Expected 'q' text search parameter or other keywords."),
      actual = response,
    )
  }

  @Test
  fun `Deserialize success`() {
    val json = getJson(filename = "search-success.json")
    val response = GalleryJson.decodeFromString<SearchResponse>(json)
    assertEquals(
      expected = SearchResponse.Success(
        collection = SearchCollection(
          version = "1.0",
          url = "http://images-api.nasa.gov/search?q=apollo&page_size=1",
          items = listOf(
            SearchItem(
              collectionUrl = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB" +
                "-ENROUTE-TO-PAD-39A/collection.json",
              data = listOf(
                SearchItemData(
                  center = "JSC",
                  title = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
                  keywords = Keywords.from("Apollo", "Apollo 8", "NASA", "Film", "Film Transfers"),
                  nasaId = NasaId("T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A"),
                  dateCreated = Instant.parse("2017-11-16T00:00:00Z"),
                  mediaType = MediaType.Video,
                  description508 = "Apollo, Apollo 8, NASA, Film, Film Transfers",
                  description = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
                ),
              ),
              links = listOf(
                SearchItemLink(
                  url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB" +
                    "-ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A~thumb.jpg",
                  rel = "preview",
                  render = "image",
                ),
                SearchItemLink(
                  url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
                    "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A.srt",
                  rel = "captions",
                ),
              ),
            ),
          ),
          metadata = SearchMetadata(totalHits = 6324),
          links = listOf(
            SearchLink(
              rel = "next",
              prompt = "Next",
              url = "http://images-api.nasa.gov/search?q=apollo&page_size=1&page=2",
            ),
          ),
        ),
      ),
      actual = response,
    )
  }

  private fun getJson(filename: String): String = getResourceAsStream(filename).reader().readText()
}
