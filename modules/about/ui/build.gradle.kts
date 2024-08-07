plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.about.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.kotlinx.coroutines)
  api(libs.voyager.core)
  api(projects.modules.about.vm)
  api(projects.modules.core.ui)

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
  implementation(projects.modules.about.res)
  implementation(projects.modules.licenses.nav)
}
