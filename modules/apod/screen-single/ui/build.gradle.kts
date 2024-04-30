plugins {
  id("module-compose")
}

android {
  namespace = "apod.single.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.voyager.core)
  implementation(projects.modules.apod.screenSingle.res)
  implementation(projects.modules.apod.screenSingle.vm)
  implementation(projects.modules.core.res)
  implementation(projects.modules.core.ui)
  implementation(projects.modules.navigation)
  implementation(libs.androidx.compose.animation.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
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
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
}
