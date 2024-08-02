package nasa.android.app

import alakazam.android.core.IBuildConfig
import android.content.Context
import android.os.Build
import kotlinx.datetime.Instant
import nasa.android.BuildConfig
import nasa.core.res.R
import javax.inject.Inject

/**
 * Gives other modules access to the app module's build metadata.
 */
internal class NasaBuildConfig @Inject constructor(context: Context) : IBuildConfig {
  override val debug = BuildConfig.DEBUG
  override val applicationId = BuildConfig.APPLICATION_ID
  override val versionCode = BuildConfig.VERSION_CODE
  override val versionName = BuildConfig.VERSION_NAME
  override val buildTime: Instant = BuildConfig.BUILD_TIME
  override val gitId = BuildConfig.GIT_HASH
  override val manufacturer: String = Build.MANUFACTURER
  override val model: String = Build.MODEL
  override val os = Build.VERSION.SDK_INT
  override val platform = context.getString(R.string.app_name)
  override val repoName = "jonapoul/nasa-android"
  override val repoUrl = "https://github.com/$repoName"
}
