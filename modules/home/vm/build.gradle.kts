plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.home.vm"
}

dependencies {
  implementation(libs.javaxInject)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
