plugins {
  id("module-compose")
}

android {
  namespace = "nasa.core.ui"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.androidx.compose.runtime)
  api(libs.kotlinx.immutable)
  api(libs.voyager.core)
  implementation(libs.alakazam.android.compose)
  implementation(libs.androidx.activity.core)
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.compose.animation.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.geometry)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.voyager.hilt)
}
