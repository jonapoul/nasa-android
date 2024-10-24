plugins {
  id("nasa.module.multiplatform")
  id("nasa.convention.coroutines")
}

android {
  namespace = "nasa.gallery.data.repo"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.preferences.core)
      api(projects.db)
      api(projects.gallery.data.api)

      implementation(libs.alakazam.kotlin.core)
      implementation(libs.javaxInject)
      implementation(libs.kotlinx.coroutines)
      implementation(libs.kotlinx.serialization.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.okhttp.core)
      implementation(libs.retrofit.core)
    }

    androidUnitTest.dependencies {
      implementation(libs.androidx.room.runtime)
      implementation(libs.test.alakazam.db)
      implementation(libs.test.kotlin.common)
      implementation(libs.test.okhttp)
      implementation(libs.test.turbine)
      implementation(projects.test.flow)
      implementation(projects.test.http)
      implementation(projects.test.prefs)
    }
  }
}
