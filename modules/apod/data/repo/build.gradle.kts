plugins {
  alias(libs.plugins.module.multiplatform)
}

android {
  namespace = "nasa.apod.data.repo"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.apod.data.api)
      api(projects.db)

      implementation(libs.alakazam.kotlin.core)
      implementation(libs.javaxInject)
      implementation(libs.kotlinx.coroutines)
      implementation(libs.kotlinx.datetime)
      implementation(libs.kotlinx.serialization.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.okhttp.core)
      implementation(libs.retrofit.core)
    }

    androidUnitTest.dependencies {
      implementation(libs.androidx.room.runtime)
      implementation(libs.test.alakazam.db)
      implementation(libs.test.okhttp)
      implementation(projects.test.flow)
      implementation(projects.test.http)
    }
  }
}
