plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.kotlinx.coroutines)

  implementation(libs.androidx.room.common)
  implementation(libs.kotlinx.datetime)
}
