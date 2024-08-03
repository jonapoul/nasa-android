package nasa.core.http

import nasa.core.model.FileSize

sealed interface DownloadState {
  val url: String
  val total: FileSize

  data class InProgress(
    override val url: String,
    val read: FileSize,
    override val total: FileSize,
  ) : DownloadState

  data class Done(
    override val url: String,
    override val total: FileSize,
  ) : DownloadState
}
