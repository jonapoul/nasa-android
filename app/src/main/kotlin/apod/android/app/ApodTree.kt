package apod.android.app

import timber.log.Timber

internal class ApodTree : Timber.DebugTree() {
  override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
    super.log(priority, tag, "APOD: $message", t)
  }

  // Prepend the class name and line number (clickable in logcat) to each message
  override fun createStackElementTag(element: StackTraceElement): String {
    return "(${element.fileName}:${element.lineNumber})"
  }
}
