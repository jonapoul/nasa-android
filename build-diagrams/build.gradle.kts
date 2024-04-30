plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_19
  targetCompatibility = JavaVersion.VERSION_19
}

kotlin {
  jvmToolchain(jdkVersion = 19)
}

dependencies {
  api(libs.plugin.blueprint)
  api(libs.plugin.dependencyGraph)
}
