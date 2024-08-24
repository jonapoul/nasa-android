plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.apod.single.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.kotlinx.datetime)
  api(libs.voyager.core)
  api(projects.apod.grid.nav)
  api(projects.apod.single.vm)
  api(projects.core.ui)

  implementation(libs.alakazam.android.compose)
  implementation(libs.androidx.activity.core)
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
  implementation(libs.coil.base)
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
  implementation(projects.apod.res)
  implementation(projects.apod.single.nav)
  implementation(projects.settings.nav)
}
