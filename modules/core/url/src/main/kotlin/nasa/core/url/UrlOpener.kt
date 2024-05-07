package nasa.core.url

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import timber.log.Timber
import javax.inject.Inject

class UrlOpener @Inject constructor(private val context: Context) {
  fun openUrl(url: String) {
    Timber.v("openUrl $url")
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    ContextCompat.startActivity(context, intent, null)
  }
}
