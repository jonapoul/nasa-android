package apod.licenses.di

import android.content.Context
import apod.licenses.data.AssetsProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class AssetsModule {
  @Provides
  fun assetsProvider(context: Context): AssetsProvider = AssetsProvider {
    context.assets.open("open_source_licenses.json")
  }
}
