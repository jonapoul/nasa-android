package nasa.core.http.progress

import nasa.core.model.FileSize
import nasa.core.model.Percent

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

fun DownloadState?.toProgress(): Percent = when (this) {
  is DownloadState.Done -> Percent.OneHundred
  is DownloadState.InProgress -> Percent(numerator = read.toBytes(), denominator = total.toBytes())
  null -> Percent.Zero
}
