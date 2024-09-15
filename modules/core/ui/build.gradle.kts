plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.core.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.coil.base)
  api(libs.kotlinx.immutable)
  api(libs.lazycolumn.scrollbar)
  api(projects.core.model)
  api(projects.core.res)

  implementation(libs.alakazam.android.compose)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.compose.animation.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.geometry)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.androidx.coreKtx)
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
}
