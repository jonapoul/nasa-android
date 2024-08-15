plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.gallery.image.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.voyager.core)
  api(projects.modules.core.ui)
  api(projects.modules.gallery.image.vm)

  implementation(libs.alakazam.android.compose)
  implementation(libs.androidx.activity.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.coil.base)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
  implementation(projects.modules.core.http)
  implementation(projects.modules.gallery.res)
}
