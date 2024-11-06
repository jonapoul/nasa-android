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
  implementation(libs.plugin.dependencyAnalysis)
  implementation(libs.plugin.dependencyGraph)
  implementation(libs.plugin.dependencySort)
  implementation(libs.plugin.detekt)
  implementation(libs.plugin.hilt)
  implementation(libs.plugin.kotlin.gradle)
  implementation(libs.plugin.kotlin.powerAssert)
  implementation(libs.plugin.kover)
  implementation(libs.plugin.ksp)
  implementation(libs.plugin.ktlint)
  implementation(libs.plugin.licensee)
  implementation(libs.plugin.spotless)
  implementation(libs.plugin.turtle)

  // https://stackoverflow.com/a/70878181/15634757
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}

gradlePlugin {
  plugins {
    create(id = "nasa.convention.android.base", impl = "nasa.gradle.ConventionAndroidBase")
    create(id = "nasa.convention.android.library", impl = "nasa.gradle.ConventionAndroidLibrary")
    create(id = "nasa.convention.compose", impl = "nasa.gradle.ConventionCompose")
    create(id = "nasa.convention.detekt", impl = "nasa.gradle.ConventionDetekt")
    create(id = "nasa.convention.diagrams", impl = "nasa.gradle.ConventionDiagrams")
    create(id = "nasa.convention.hilt", impl = "nasa.gradle.ConventionHilt")
    create(id = "nasa.convention.kotlin", impl = "nasa.gradle.ConventionKotlin")
    create(id = "nasa.convention.kover", impl = "nasa.gradle.ConventionKover")
    create(id = "nasa.convention.ktlint", impl = "nasa.gradle.ConventionKtlint")
    create(id = "nasa.convention.licensee", impl = "nasa.gradle.ConventionLicensee")
    create(id = "nasa.convention.spotless", impl = "nasa.gradle.ConventionSpotless")
    create(id = "nasa.convention.style", impl = "nasa.gradle.ConventionStyle")
    create(id = "nasa.convention.test", impl = "nasa.gradle.ConventionTest")
    create(id = "nasa.module.android", impl = "nasa.gradle.ModuleAndroid")
    create(id = "nasa.module.compose", impl = "nasa.gradle.ModuleCompose")
    create(id = "nasa.module.hilt", impl = "nasa.gradle.ModuleHilt")
    create(id = "nasa.module.kotlin", impl = "nasa.gradle.ModuleKotlin")
    create(id = "nasa.module.multiplatform", impl = "nasa.gradle.ModuleMultiplatform")
    create(id = "nasa.module.navigation", impl = "nasa.gradle.ModuleNavigation")
    create(id = "nasa.module.resources", impl = "nasa.gradle.ModuleResources")
    create(id = "nasa.module.viewmodel", impl = "nasa.gradle.ModuleViewModel")
  }
}

fun NamedDomainObjectContainer<PluginDeclaration>.create(id: String, impl: String) = create(id) {
  this.id = id
  implementationClass = impl
}
