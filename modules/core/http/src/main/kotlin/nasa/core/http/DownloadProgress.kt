package nasa.core.http

import alakazam.kotlin.core.StateHolder
import nasa.core.model.FileSize
import nasa.core.model.Percent
import nasa.core.model.bytes
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer
import javax.inject.Inject
import javax.inject.Singleton

class DownloadProgressInterceptor(private val stateHolder: DownloadProgressStateHolder) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalResponse = chain.proceed(chain.request())
    val responseBuilder = originalResponse.newBuilder()
    val originalBody = originalResponse.body

    val identifier = originalResponse.request.header(DOWNLOAD_IDENTIFIER_HEADER)
    if (!identifier.isNullOrEmpty() && originalBody != null) {
      val body = DownloadProgressResponseBody(
        url = originalResponse.request.url.toString(),
        responseBody = originalBody,
        stateHolder = stateHolder,
      )
      responseBuilder.body(body)
    }

    return responseBuilder.build()
  }
}

const val DOWNLOAD_IDENTIFIER_HEADER = "download-identifier"

@Singleton
class DownloadProgressStateHolder @Inject constructor() : StateHolder<DownloadState?>(initialState = null)

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

private class DownloadProgressResponseBody(
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
