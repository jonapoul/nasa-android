package nasa.android.app

import timber.log.Timber

internal class NasaTree : Timber.DebugTree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    super.log(priority, tag, "NASA: $message", t)
  }

  // Prepend the class name and line number (clickable in logcat) to each message
  override fun createStackElementTag(element: StackTraceElement): String = "(${element.fileName}:${element.lineNumber})"
}
