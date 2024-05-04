plugins {
  id("module-compose")
}

android {
  namespace = "apod.grid.ui"
}

dependencies {
  api(projects.modules.apod.screenGrid.vm)
  api(projects.modules.core.ui)
  api(projects.modules.navigation)
  api(libs.androidx.compose.runtime)
  api(libs.kotlinx.datetime)
  api(libs.voyager.core)
  implementation(projects.modules.apod.screenGrid.res)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
}
