package nasa.gallery.data.api

import kotlinx.datetime.Instant
import nasa.gallery.model.Center
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.NasaId
import nasa.test.getResourceAsText
import org.junit.Test
import kotlin.test.assertEquals

class SearchResponseTest {
  @Test
  fun `Deserialize failure`() {
    val json = getResourceAsText(filename = "search-failure.json")
    val response = GalleryJson.decodeFromString<SearchResponse>(json)
    assertEquals(
      expected = SearchResponse.Failure(reason = "Expected 'q' text search parameter or other keywords."),
      actual = response,
    )
  }

  @Test
  fun `Deserialize success`() {
    val json = getResourceAsText(filename = "search-success.json")
    val response = GalleryJson.decodeFromString<SearchResponse>(json)
    assertEquals(
      expected = SearchResponse.Success(
        collection = Collection(
          version = "1.0",
          url = "http://images-api.nasa.gov/search?q=apollo&page_size=1",
          items = listOf(
            CollectionItem(
              collectionUrl = JsonUrl(
                "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-" +
                  "TO-PAD-39A/collection.json",
              ),
              data = listOf(
                SearchItem(
                  center = Center("JSC"),
                  title = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
                  keywords = Keywords.from("Apollo", "Apollo 8", "NASA", "Film", "Film Transfers"),
                  nasaId = NasaId("T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A"),
                  dateCreated = Instant.parse("2017-11-16T00:00:00Z"),
                  mediaType = MediaType.Video,
                  description508 = "Apollo, Apollo 8, NASA, Film, Film Transfers",
                  description = "T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A",
                  location = null,
                  photographer = null,
                  secondaryCreator = null,
                  album = null,
                ),
              ),
              links = listOf(
                CollectionItemLink(
                  url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB" +
                    "-ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A~thumb.jpg",
                  rel = CollectionItemLink.Relation.Preview,
                  render = "image",
                ),
                CollectionItemLink(
                  url = "https://images-assets.nasa.gov/video/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-" +
                    "ENROUTE-TO-PAD-39A/T803303_A_AS-503-APOLLO-8-CREW-DEPARTING-MSOB-ENROUTE-TO-PAD-39A.srt",
                  rel = CollectionItemLink.Relation.Captions,
                ),
              ),
            ),
          ),
          metadata = CollectionMetadata(totalHits = 6324),
          links = listOf(
            CollectionLink(
              rel = CollectionLink.Relation.Next,
              prompt = "Next",
              url = "http://images-api.nasa.gov/search?q=apollo&page_size=1&page=2",
            ),
          ),
        ),
      ),
      actual = response,
    )
  }
}
