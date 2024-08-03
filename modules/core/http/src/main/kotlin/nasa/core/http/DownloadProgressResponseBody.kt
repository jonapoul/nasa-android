package nasa.core.http

import nasa.core.model.bytes
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

internal class DownloadProgressResponseBody(
  private val url: String,
  private val responseBody: ResponseBody,
  private val stateHolder: DownloadProgressStateHolder,
) : ResponseBody() {
  private var bufferedSource: BufferedSource? = null

  override fun contentLength() = responseBody.contentLength()

  override fun contentType() = responseBody.contentType()

  override fun source() = bufferedSource
    ?: getForwardSource(responseBody.source())
      .buffer()
      .also { bufferedSource = it }

  private fun getForwardSource(source: Source): Source = object : ForwardingSource(source) {
    private var totalBytesRead = 0L

    override fun read(sink: Buffer, byteCount: Long): Long {
      // read() returns the number of bytes read, or -1 if this source is exhausted.
      val bytesRead = super.read(sink, byteCount)
      totalBytesRead += if (bytesRead != -1L) bytesRead else 0
      val state = if (bytesRead == -1L) {
        DownloadState.Done(url, totalBytesRead.bytes)
      } else {
        DownloadState.InProgress(url, totalBytesRead.bytes, contentLength().bytes)
      }
      stateHolder.set(state)
      return bytesRead
    }
  }
}
