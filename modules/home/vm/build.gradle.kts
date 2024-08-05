plugins {
  id("module-viewmodel")
}

android {
  namespace = "nasa.home.vm"
}

dependencies {
  api(projects.modules.core.http)

  implementation(libs.javaxInject)
  implementation(projects.modules.core.url)

  compileOnly(libs.alakazam.kotlin.compose.annotations)
}
