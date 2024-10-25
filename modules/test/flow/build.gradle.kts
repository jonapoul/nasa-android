import nasa.gradle.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

commonMainDependencies {
  api(libs.test.kotlin.common)
  api(libs.test.turbine)
}
