package nasa.android.di

import alakazam.android.core.IBuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.core.http.buildOkHttp
import nasa.core.http.buildRetrofit
import nasa.gallery.data.api.GalleryApi
import nasa.gallery.data.api.GalleryJson
import retrofit2.create
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
internal class GalleryModule {
  @Provides
  fun api(buildConfig: IBuildConfig): GalleryApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug) { Timber.tag("GALLERY").v(it) },
    json = GalleryJson,
    url = GALLERY_API_URL,
  ).create()

  private companion object {
    const val GALLERY_API_URL = "https://images-api.nasa.gov"
  }
}
