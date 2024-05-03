plugins {
  id("module-compose")
}

android {
  namespace = "apod.settings.ui"
}

dependencies {
  api(projects.modules.core.model)
  api(libs.androidx.compose.runtime)
  api(libs.voyager.core)
  implementation(projects.modules.core.ui)
  implementation(projects.modules.settings.res)
  implementation(libs.alakazam.android.compose)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.androidx.compose.animation.core)
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
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.androidx.coreKtx)
  implementation(libs.androidx.preference.ktx)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable)
  implementation(libs.voyager.navigator)
}
