plugins {
  id("com.google.devtools.ksp")
}

val implementation by configurations
val testImplementation by configurations
val ksp by configurations

dependencies {
  implementation(libs.hilt.core)
  ksp(libs.hilt.compiler)
  testImplementation(libs.test.hilt)
}
