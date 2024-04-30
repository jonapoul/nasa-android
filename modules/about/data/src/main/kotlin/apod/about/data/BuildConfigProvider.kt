package apod.about.data

import kotlinx.datetime.Instant

interface BuildConfigProvider {
  val versionName: String
  val buildTime: Instant
}

data class BuildConfigProviderImpl(
  override val versionName: String,
  override val buildTime: Instant,
) : BuildConfigProvider
