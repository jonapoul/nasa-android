package nasa.core.http

data class DownloadState(
  val url: String,
  val bytesRead: Long,
  val contentLength: Long,
  val done: Boolean,
)
