plugins {
  id("nasa.module.kotlin")
  alias(libs.plugins.kotlin.serialization)
}

dependencies {
  api(libs.kotlinx.datetime)
  api(projects.apod.model)
}
