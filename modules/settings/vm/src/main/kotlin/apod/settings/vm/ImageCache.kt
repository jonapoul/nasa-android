package apod.settings.vm

import android.content.Context
import apod.core.model.IMAGE_CACHE_DIR
import timber.log.Timber
import javax.inject.Inject

internal class ImageCache @Inject constructor(context: Context) {
  internal val cacheDir = context.cacheDir.resolve(IMAGE_CACHE_DIR)

  fun calculateSize(): FileSize {
    return cacheDir.walkTopDown()
      .filter { it.isFile }
      .sumOf { it.length() }
      .bytes
  }

  fun clear(): Boolean {
    return try {
      cacheDir.deleteRecursively()
    } catch (e: Exception) {
      Timber.w(e, "Failed clearing $cacheDir")
      false
    }
  }
}
