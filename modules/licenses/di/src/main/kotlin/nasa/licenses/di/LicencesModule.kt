package nasa.licenses.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.licenses.data.AssetsProvider

@Module
@InstallIn(SingletonComponent::class)
class LicencesModule {
  @Provides
  fun assetsProvider(context: Context): AssetsProvider = AssetsProvider {
    context.assets.open("open_source_licenses.json")
  }
}
