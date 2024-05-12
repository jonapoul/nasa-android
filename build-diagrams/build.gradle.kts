plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
  jvmToolchain(jdkVersion = 17)
}

dependencies {
  api(libs.plugin.blueprint)
  api(libs.plugin.dependencyGraph)
}
