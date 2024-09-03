plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.apod.ui.single"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.androidx.navigation.runtime)
  api(libs.kotlinx.datetime)
  api(projects.apod.nav)
  api(projects.apod.vm.single)
  api(projects.core.ui)

  implementation(libs.alakazam.android.compose)
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
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.androidx.navigation.common)
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.apod.preview)
  implementation(projects.apod.res)
  implementation(projects.settings.nav)
}
