plugins {
  id("nasa.module.multiplatform")
}

android {
  namespace = "nasa.test.flow"
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.test.kotlin.common)
      api(libs.test.kotlin.junit)
      api(libs.test.turbine)
    }
  }
}
