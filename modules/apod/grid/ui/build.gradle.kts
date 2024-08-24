plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.apod.grid.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.lifecycle.viewmodel.savedstate)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.datetime)
  api(libs.voyager.core)
  api(projects.apod.data.repo)
  api(projects.apod.grid.nav)
  api(projects.apod.nav)
  api(projects.apod.single.nav)
  api(projects.core.ui)
  api(projects.core.url)

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
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
  implementation(projects.apod.res)
  implementation(projects.settings.nav)
}
