package nasa.licenses.data

import android.content.Context
import java.io.InputStream

class AndroidAssetsProvider(private val context: Context) : AssetsProvider {
  override fun licensesJsonStream(): InputStream = context.assets.open("open_source_licenses.json")
}
