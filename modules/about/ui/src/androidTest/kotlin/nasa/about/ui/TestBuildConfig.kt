package nasa.about.ui

import alakazam.android.core.IBuildConfig

internal object TestBuildConfig : IBuildConfig {
  override val applicationId = ""
  override val buildTime = TestInstant
  override val debug = false
  override val gitId = "abcd1234"
  override val manufacturer = "Acme"
  override val model = "Thing"
  override val os = 30
  override val platform = "NASA Android"
  override val repoName = "jonapoul/nasa-android"
  override val repoUrl = "https://github.com/$repoName"
  override val versionCode = 123
  override val versionName = "1.2.3"
}
