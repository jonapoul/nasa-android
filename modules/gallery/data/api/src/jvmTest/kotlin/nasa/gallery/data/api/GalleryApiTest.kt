package nasa.gallery.data.api

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import nasa.gallery.model.Album
import nasa.gallery.model.BooleanMetadata
import nasa.gallery.model.Center
import nasa.gallery.model.DoubleMetadata
import nasa.gallery.model.ImageUrl
import nasa.gallery.model.IntMetadata
import nasa.gallery.model.JsonUrl
import nasa.gallery.model.Keywords
import nasa.gallery.model.MediaType
import nasa.gallery.model.MediaTypes
import nasa.gallery.model.Metadata
import nasa.gallery.model.NasaId
import nasa.gallery.model.Object
import nasa.gallery.model.ObjectListMetadata
import nasa.gallery.model.ObjectMetadata
import nasa.gallery.model.StringListMetadata
import nasa.gallery.model.StringMetadata
import nasa.gallery.model.UrlCollection
import nasa.gallery.model.year
import nasa.test.MockWebServerRule
import nasa.test.getResourceAsText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail

class GalleryApiTest {
  @get:Rule
  val webServerRule = MockWebServerRule()

  private lateinit var galleryApi: GalleryApi

  @Before
  fun before() {
    galleryApi = webServerRule.buildApi(json = GalleryJson)
  }

  @Test
  fun `Encode single media type`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode two media types`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video, MediaType.Audio))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video,audio",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode three media types`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(mediaTypes = MediaTypes(MediaType.Video, MediaType.Audio, MediaType.Image))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "video,audio,image",
      actual = request.requestUrl?.queryParameter(name = "media_type"),
    )
  }

  @Test
  fun `Encode years`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(
      yearStart = 1900.year,
      yearEnd = 2024.year,
    )

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(expected = "1900", actual = request.requestUrl?.queryParameter(name = "year_start"))
    assertEquals(expected = "2024", actual = request.requestUrl?.queryParameter(name = "year_end"))
  }

  @Test
  fun `Encode NASA ID`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(nasaId = NasaId("abc"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc",
      actual = request.requestUrl?.queryParameter(name = "nasa_id"),
    )
  }

  @Test
  fun `Encode single keyword`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(keywords = Keywords("abc"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc",
      actual = request.requestUrl?.queryParameter(name = "keywords"),
    )
  }

  @Test
  fun `Encode multiple keywords`() = runTest {
    enqueueDefault()

    // When
    galleryApi.search(keywords = Keywords("abc", "def", "xyz"))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "abc,def,xyz",
      actual = request.requestUrl?.queryParameter(name = "keywords"),
    )
  }

  @Test
  fun `Deserialize search response failure`() = runTest {
    // Given
    val json = getResourceAsText(filename = "search-failure.json")
    webServerRule.enqueue(json, code = HTTP_BAD_REQUEST)

    // When we search with no parameters
    val response = galleryApi.search()

    // Then
    assertFalse(response.isSuccessful)
    val errorBody = requireNotNull(response.errorBody())
    val error = GalleryJson.decodeFromString(SearchResponse.Failure.serializer(), errorBody.string())
    assertEquals(
      actual = error,
      expected = SearchResponse.Failure(reason = "Expected 'q' text search parameter or other keywords."),
    )
  }

  @Suppress("LongMethod")
  @Test
  fun `Deserialize search response success`() = runTest {
    // Given
    val json = getResourceAsText(filename = "search-success-no-links.json")
    webServerRule.enqueue(json)

    // When we search
    val response = galleryApi.search(
      description = "Chiaki",
      keywords = Keywords("SPACELAB", "CREWS"),
    )

    // Then
    val item1 = CollectionItem(
      collectionUrl = JsonUrl("https://images-assets.nasa.gov/image/sts065-05-037/collection.json"),
      data = listOf(
        SearchItem(
          center = Center("JSC"),
          title = "STS-65 crew works inside the IML-2 spacelab module aboard Columbia, OV-102",
          keywords = Keywords(
            "STS-65",
            "COLUMBIA (ORBITER)",
            "SPACELAB",
            "CREWS",
            "ASTRONAUTS",
            "ONBOARD ACTIVITIES",
            "CREW PROCEDURES (INFLIGHT)",
            "SPACEBORNE EXPERIMENTS",
            "RACKS",
            "JAPAN PAYLOAD SPECIALISTS",
            "INTERNATIONAL COOPERATION",
          ),
          nasaId = NasaId("sts065-05-037"),
          dateCreated = Instant.parse("1994-07-23T00:00:00Z"),
          mediaType = MediaType.Image,
          description = "STS065-05-037 (8-23 July 1994) --- In the science module aboard the Space Shuttle " +
            "Columbia, four members of the crew busy themselves with experiments in support of the second " +
            "International Microgravity Laboratory (IML-2) mission.  Left to right are Donald A. Thomas and " +
            "Leroy Chiao, both mission specialists; Richard J. Hieb, payload commander, and Dr. Chiaki Mukai " +
            "of NASDA, payload specialist.",
          description508 = null,
          location = null,
          photographer = null,
          secondaryCreator = null,
          album = null,
        ),
      ),
      links = listOf(
        CollectionItemLink(
          url = "https://images-assets.nasa.gov/image/sts065-05-037/sts065-05-037~thumb.jpg",
          rel = CollectionItemLink.Relation.Preview,
          render = "image",
        ),
      ),
    )
    val item2 = CollectionItem(
      collectionUrl = JsonUrl("https://images-assets.nasa.gov/image/sts065-214-010/collection.json"),
      data = listOf(
        SearchItem(
          center = Center("JSC"),
          title = "STS-65 crew onboard portrait in IML-2 spacelab module with mission flag",
          keywords = Keywords(
            "STS-65",
            "COLUMBIA (ORBITER)",
            "SPACELAB",
            "CREWS",
            "ASTRONAUTS",
            "PAYLOAD SPECIALISTS",
            "PORTRAIT",
            "ONBOARD ACTIVITIES",
            "INTERNATIONAL COOPERATION",
            "JAPAN",
            "INSIGNIAS CREW PROCEDURES (INFLIGHT)",
          ),
          nasaId = NasaId("sts065-214-010"),
          dateCreated = Instant.parse("1994-07-23T00:00:00Z"),
          mediaType = MediaType.Image,
          description = "In the spacelab science module aboard the Space Shuttle Columbia, Orbiter Vehicle (OV) 102," +
            " the seven crewmembers pose for the traditional onboard (inflight) crew portrait. Displayed in the " +
            "background is a flag with the International Microgravity Laboratory 2 (IML-2) insignia and Columbia " +
            "inscribed along the edge. In the front row (left to right) are Mission Specialist (MS) Carl E. Walz " +
            "and MS Donald A. Thomas. Behind them (left to right) are Payload Commander (PLC) Richard J. Hieb, " +
            "Payload Specialist Chiaki Mukai, Commander Robert D. Cabana, MS Leroy Chiao, and Pilot James D. " +
            "Halsell, Jr. Mukai represents the National Space Development Agency (NASDA) of Japan. Crewmembers are " +
            "wearing their mission polo shirts for the portrait. Inside this module, the crew conducted experiments" +
            " in support of the IML-2 mission.",
          description508 = null,
          location = null,
          photographer = null,
          secondaryCreator = null,
          album = null,
        ),
      ),
      links = listOf(
        CollectionItemLink(
          url = "https://images-assets.nasa.gov/image/sts065-214-010/sts065-214-010~thumb.jpg",
          rel = CollectionItemLink.Relation.Preview,
          render = "image",
        ),
      ),
    )
    assertEquals(
      actual = response.body(),
      expected = SearchResponse.Success(
        collection = Collection(
          version = "1.0",
          url = "http://images-api.nasa.gov/search?description=Chiaki&keywords=CREWS,SPACELAB",
          metadata = CollectionMetadata(totalHits = 2),
          links = null,
          items = listOf(item1, item2),
        ),
      ),
    )
  }

  @Test
  fun `Encode asset manifest URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.getAssetManifest(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/asset/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Encode metadata URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.locateMetadata(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/metadata/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Encode captions URL`() = runTest {
    enqueueDefault()

    // When
    val id = "abc"
    galleryApi.locateCaptions(NasaId(id))

    // Then
    val request = webServerRule.server.takeRequest()
    assertEquals(
      expected = "/captions/abc",
      actual = request.requestUrl?.encodedPath,
    )
  }

  @Test
  fun `Get collection`() = runTest {
    // Given
    val collectionJson = getResourceAsText(filename = "collection-success.json")
    webServerRule.enqueue(collectionJson)

    // When
    val response = galleryApi.getCollection(url = JsonUrl("url.com"))

    // Then
    assertTrue(response.isSuccessful)
    val collection = response.body()
    assertEquals(
      actual = collection,
      expected = UrlCollection(
        ImageUrl("http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~orig.jpg"),
        ImageUrl("http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~large.jpg"),
        ImageUrl("http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~medium.jpg"),
        ImageUrl("http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~small.jpg"),
        ImageUrl("http://images-assets.nasa.gov/image/NHQ202401260011/NHQ202401260011~thumb.jpg"),
        JsonUrl("http://images-assets.nasa.gov/image/NHQ202401260011/metadata.json"),
      ),
    )
    assertEquals(expected = 6, actual = collection?.size)
  }

  @Test
  fun `Get metadata`() = runTest {
    // Given
    val collectionJson = getResourceAsText(filename = "metadata-success.json")
    webServerRule.enqueue(collectionJson)

    // When
    val response = galleryApi.getMetadata(url = JsonUrl("url.com"))

    // Then the response was parsed
    assertTrue(response.isSuccessful)
    val metadata = response.body() ?: fail("Null body for $response")
    assertEquals(expected = 349, actual = metadata.size)

    // Checking some specific cases:
    with(metadata) {
      assertElement<String, StringMetadata>(key = "APP14:APP14Flags1", value = "(none)")
      assertElement<String, StringMetadata>(key = "AVAIL:Description508", value = "")
      assertElement<Double, DoubleMetadata>(key = "Composite:LightValue", value = 6.9)
      assertElement<Int, IntMetadata>(key = "EXIF:ISO", value = 3200)
      assertElement<Boolean, BooleanMetadata>(key = "XMP:AlreadyApplied", value = true)
      assertElement<ImmutableList<String>, StringListMetadata>(
        key = "XMP:PersonInImage",
        value = persistentListOf("Jim Free", "Nyam-Osor Uchral"),
      )
      assertElement<Object, ObjectMetadata>(
        key = "XMP:CreatorContactInfo",
        value = buildObject(
          "CiAdrCity" to "Washington",
          "CiAdrCtry" to "USA",
          "CiAdrExtadr" to "NASA Headquarters\n300 E Street, SW",
          "CiAdrPcode" to 20546,
          "CiAdrRegion" to "DC",
          "CiTelWork" to "202-358-1900",
          "CiUrlWork" to "http://www.nasa.gov",
        ),
      )
      assertElement<ImmutableList<Object>, ObjectListMetadata>(
        key = "XMP:History",
        value = persistentListOf(
          buildObject(
            "Action" to "saved",
            "Changed" to "/metadata",
            "InstanceID" to "xmp.iid:282bd4d9-03f4-4030-9a1e-a572912363d8",
            "SoftwareAgent" to "Adobe Photoshop Lightroom Classic 13.1 (Macintosh)",
            "When" to "2024:01:26 10:50:32-05:00",
          ),
          buildObject(
            "Action" to "derived",
            "Parameters" to "converted from image/x-nikon-nef to image/jpeg, saved to new location",
          ),
          buildObject(
            "Action" to "saved",
            "Changed" to "/",
            "InstanceID" to "xmp.iid:ce367407-eadb-4e50-841b-524c8ab8f0a7",
            "SoftwareAgent" to "Adobe Photoshop Lightroom Classic 13.1 (Macintosh)",
            "When" to "2024:01:26 12:21:52-05:00",
          ),
        ),
      )
    }
  }

  @Test
  fun `Get album contents`() = runTest {
    // Given
    val json = getResourceAsText(filename = "albums-success.json")
    webServerRule.enqueue(json)

    // When
    val response = galleryApi.getAlbumContents(album = Album("NICER"))

    // Then
    assertTrue(response.isSuccessful)
    val body = response.body() ?: fail("Null body for $response")
    assertEquals(
      actual = body,
      expected = SearchResponse.Success(
        collection = Collection(
          version = "1.0",
          url = "http://images-api.nasa.gov/album/NICER",
          metadata = CollectionMetadata(totalHits = 1),
          links = null,
          items = listOf(
            CollectionItem(
              collectionUrl = JsonUrl(
                "https://images-assets.nasa.gov/video/GSFC_20190130_NICER_m12854_BlkHole/collection.json",
              ),
              links = listOf(
                CollectionItemLink(
                  url = "https://images-assets.nasa.gov/video/GSFC_20190130_NICER_m12854_BlkHole/" +
                    "GSFC_20190130_NICER_m12854_BlkHole~thumb.jpg",
                  rel = CollectionItemLink.Relation.Preview,
                  render = "image",
                ),
                CollectionItemLink(
                  url = "https://images-assets.nasa.gov/video/GSFC_20190130_NICER_m12854_BlkHole/" +
                    "GSFC_20190130_NICER_m12854_BlkHole.srt",
                  rel = CollectionItemLink.Relation.Captions,
                ),
              ),
              data = listOf(
                SearchItem(
                  album = listOf(
                    Album("NICER"),
                    Album("ISS_Research_Videos"),
                  ),
                  center = Center(value = "GSFC"),
                  title = "NICER Charts the Area Around a New Black Hole",
                  keywords = Keywords("Neutron Star", "Black Hole", "X-Ray", "Astrophysics"),
                  location = "Goddard Space Flight Center",
                  photographer = null,
                  nasaId = NasaId("GSFC_20190130_NICER_m12854_BlkHole"),
                  dateCreated = Instant.parse("2019-01-30T00:00:00Z"),
                  mediaType = MediaType.Video,
                  secondaryCreator = "Scott Wiessinger",
                  description = "Foobar",
                  description508 = "Watch how X-ray echoes, mapped by NASA’s Neutron star Interior Composition " +
                    "Explorer (NICER) revealed changes to the corona of black hole MAXI J1820+070.",
                ),
              ),
            ),
          ),
        ),
      ),
    )
  }

  @Test
  fun `Get albums failure`() = runTest {
    // Given
    val json = getResourceAsText(filename = "albums-failure.json")
    webServerRule.enqueue(json, code = HTTP_NOT_FOUND)

    // When we search with no parameters
    val response = galleryApi.search()

    // Then
    assertFalse(response.isSuccessful)
    val errorBody = requireNotNull(response.errorBody())
    val error = GalleryJson.decodeFromString<SearchResponse.Failure>(errorBody.string())
    assertEquals(
      actual = error,
      expected = SearchResponse.Failure(reason = "No assets found for album=\"NICERs\" page=1"),
    )
  }

  private inline fun <reified T, reified M> MetadataCollection.assertElement(
    key: String,
    value: T,
  ) where M : Metadata<T>, M : Any {
    val element = get(key)
    assertIs<M>(element)
    assertEquals(expected = value, actual = element.value)
  }

  private fun buildObject(vararg content: Pair<String, Any>): Object = content.toMap().toImmutableMap()

  private fun enqueueDefault() {
    val errorJson = getResourceAsText(filename = "search-failure.json")
    webServerRule.enqueue(errorJson, code = 404)
  }
}
