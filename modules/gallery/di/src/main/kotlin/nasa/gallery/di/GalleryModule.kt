package nasa.gallery.di

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GalleryModule {
  @Provides
  @Singleton
  fun api(
    buildConfig: IBuildConfig,
  ): GalleryApi = buildRetrofit(
    client = buildOkHttp(buildConfig.debug) { Timber.tag("GALLERY").v(it) },
    json = GalleryJson,
    url = "https://images-api.nasa.gov",
  ).create()
}
