plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.home.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.voyager.core)
  api(projects.core.android)
  api(projects.core.http)

  implementation(libs.alakazam.android.compose)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.androidx.activity.core)
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
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.coil.compose)
  implementation(libs.coil.composeBase)
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(libs.javaxInject)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.lazycolumn.scrollbar)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
  implementation(projects.about.nav)
  implementation(projects.apod.data.repo)
  implementation(projects.apod.nav)
  implementation(projects.core.ui)
  implementation(projects.gallery.data.repo)
  implementation(projects.gallery.nav)
  implementation(projects.home.res)
  implementation(projects.settings.nav)
}
