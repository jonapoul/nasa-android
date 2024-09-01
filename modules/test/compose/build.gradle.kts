plugins {
  id("nasa.module.compose")
}

android {
  namespace = "nasa.test.compose"
}

dependencies {
  api(libs.test.androidx.compose.ui.junit4)
}
