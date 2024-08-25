plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.licenses.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.voyager.core)
  api(projects.core.android)
  api(projects.core.ui)
  api(projects.licenses.data)

  implementation(libs.alakazam.android.compose)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.androidx.activity.core)
  implementation(libs.androidx.annotation)
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
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable)
  implementation(libs.lazycolumn.scrollbar)
  implementation(libs.voyager.hilt)
  implementation(libs.voyager.navigator)
  implementation(projects.licenses.res)
}
