package nasa.licenses.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nasa.licenses.data.AndroidAssetsProvider
import nasa.licenses.data.AssetsProvider

@Module
@InstallIn(SingletonComponent::class)
class LicencesModule {
  @Provides
  fun assetsProvider(context: Context): AssetsProvider = AndroidAssetsProvider(context)
}
