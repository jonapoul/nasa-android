plugins {
  id("module-kotlin")
}

dependencies {
  api(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.datetime)
}
