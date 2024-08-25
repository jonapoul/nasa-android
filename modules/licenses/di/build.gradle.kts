plugins {
  id("nasa.module.hilt")
}

android {
  namespace = "nasa.licenses.di"
}

dependencies {
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.licenses.data)
}
