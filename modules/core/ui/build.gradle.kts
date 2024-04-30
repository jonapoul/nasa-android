plugins {
  id("module-compose")
}

android {
  namespace = "apod.core.ui"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.androidx.compose.runtime)
  api(libs.kotlinx.immutable)
  implementation(projects.modules.core.res)
  implementation(libs.alakazam.android.compose)
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.androidx.compose.material3)
}
