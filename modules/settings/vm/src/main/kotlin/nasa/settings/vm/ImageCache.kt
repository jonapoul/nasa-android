package nasa.settings.vm

import android.content.Context
import nasa.core.model.IMAGE_CACHE_DIR
import timber.log.Timber
import javax.inject.Inject

internal class ImageCache @Inject constructor(context: Context) {
  internal val cacheDir = context.cacheDir.resolve(IMAGE_CACHE_DIR)

  fun calculateSize(): FileSize = cacheDir
    .walkTopDown()
    .filter { it.isFile }
    .sumOf { it.length() }
    .bytes

  fun clear(): Boolean = try {
    cacheDir.deleteRecursively()
  } catch (e: Exception) {
    Timber.w(e, "Failed clearing $cacheDir")
    false
  }
}
