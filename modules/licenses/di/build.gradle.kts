plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.licenses.di"
}

dependencies {
  api(libs.javaxInject)
  api(projects.modules.licenses.data)

  implementation(libs.kotlin.stdlib)
}
