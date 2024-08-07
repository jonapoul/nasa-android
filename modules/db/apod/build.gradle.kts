plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.datetime)
}
