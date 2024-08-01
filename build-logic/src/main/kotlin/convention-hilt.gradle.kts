plugins {
  id("com.google.devtools.ksp")
}

val implementation by configurations
val testImplementation by configurations
val ksp by configurations

dependencies {
  implementation(libs.hilt.core)
  testImplementation(libs.test.hilt)
  ksp(libs.hilt.compiler)
}
