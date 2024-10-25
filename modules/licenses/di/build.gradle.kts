plugins {
  alias(libs.plugins.module.hilt)
}

dependencies {
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(projects.licenses.data)
}
