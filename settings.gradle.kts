@file:Suppress("UnstableApiUsage")

rootProject.name = "nasa-android"

pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")
includeBuild("build-diagrams")

include(":app")

include(":modules:about:data")
include(":modules:about:di")
include(":modules:about:ui")
include(":modules:about:vm")

include(":modules:apod:data:api")
include(":modules:apod:data:db")
include(":modules:apod:data:di")
include(":modules:apod:data:repo")
include(":modules:apod:model")
include(":modules:apod:screen-single:ui")
include(":modules:apod:screen-single:vm")
include(":modules:apod:screen-grid:ui")
include(":modules:apod:screen-grid:vm")

include(":modules:core:http")
include(":modules:core:model")
include(":modules:core:ui")
include(":modules:core:url")

include(":modules:home:ui")
include(":modules:home:vm")

include(":modules:licenses:data")
include(":modules:licenses:di")
include(":modules:licenses:ui")
include(":modules:licenses:vm")

include(":modules:nav:args")
include(":modules:nav:screens")

include(":modules:settings:ui")
include(":modules:settings:vm")

include(":modules:test:android")
include(":modules:test:http")
include(":modules:test:prefs")
