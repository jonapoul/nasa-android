plugins {
  id("module-hilt")
}

android {
  namespace = "nasa.licenses.di"
}

dependencies {
  api(projects.modules.licenses.data)
  api(libs.javaxInject)
}
