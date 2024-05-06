package apod.about.vm

import alakazam.android.core.IBuildConfig
import kotlinx.datetime.Instant

// Mon May 06 2024 14:23:21
internal val TestInstant = Instant.fromEpochMilliseconds(1715005401531)

internal object TestBuildConfig : IBuildConfig {
  override val applicationId = ""
  override val buildTime = TestInstant
  override val debug = false
  override val gitId = "abcd1234"
  override val manufacturer = "Acme"
  override val model = "Thing"
  override val os = 30
  override val platform = "APOD Android"
  override val repoName = "jonapoul/apod-android"
  override val repoUrl = "https://github.com/$repoName"
  override val versionCode = 123
  override val versionName = "1.2.3"
}
