plugins {
  id("module-hilt")
}

android {
  namespace = "apod.licenses.di"
}

dependencies {
  api(projects.modules.licenses.data)
  api(libs.javaxInject)
}
