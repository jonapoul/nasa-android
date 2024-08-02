import java.util.Properties

plugins {
  `kotlin-dsl`
}

// Pull java version property from project's root properties file, since build-logic doesn't have access to it
val propsFile = file("../gradle.properties")
if (!propsFile.exists()) error("No file found at ${propsFile.absolutePath}")
val props = Properties().also { it.load(propsFile.reader()) }
val javaInt = props["blueprint.javaVersion"]?.toString()?.toInt() ?: error("Failed getting java version from $props")
val javaVersion = JavaVersion.toVersion(javaInt)

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

kotlin {
  jvmToolchain(javaInt)
}

dependencies {
  implementation(libs.plugin.agp)
  implementation(libs.plugin.androidCacheFix)
  implementation(libs.plugin.blueprint.core)
  implementation(libs.plugin.blueprint.recipes)
  implementation(libs.plugin.compose)
  implementation(libs.plugin.dependencyGraph)
  implementation(libs.plugin.dependencyGuard)
  implementation(libs.plugin.dependencySort)
  implementation(libs.plugin.detekt)
  implementation(libs.plugin.hilt)
  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.kover)
  implementation(libs.plugin.ksp)
  implementation(libs.plugin.ktlint)
  implementation(libs.plugin.licensee)
  implementation(libs.plugin.spotless)
  implementation(libs.plugin.turtle)

  // https://stackoverflow.com/a/70878181/15634757
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
