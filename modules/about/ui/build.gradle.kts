plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.about.ui"
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.androidx.compose.runtime)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.androidx.navigation.runtime)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(libs.kotlinx.datetime)
  api(projects.about.data)
  api(projects.core.android)
  api(projects.core.ui)

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
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.androidx.navigation.common)
  implementation(libs.hilt.android)
  implementation(libs.hilt.core)
  implementation(projects.about.res)
  implementation(projects.licenses.nav)

  androidTestImplementation(projects.core.di)
}
