@file:Suppress("UnstableApiUsage")

rootProject.name = "apod-android"

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
    mavenLocal()
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")
includeBuild("build-diagrams")

include(":app")

include(":modules:about:data")
include(":modules:about:di")
include(":modules:about:res")
include(":modules:about:ui")
include(":modules:about:vm")

include(":modules:apod:data:api")
include(":modules:apod:data:db")
include(":modules:apod:data:di")
include(":modules:apod:data:model")
include(":modules:apod:data:repo")

include(":modules:apod:screen-single:res")
include(":modules:apod:screen-single:ui")
include(":modules:apod:screen-single:vm")

include(":modules:core:http")
include(":modules:core:res")
include(":modules:core:model")
include(":modules:core:theme")
include(":modules:core:ui")
include(":modules:core:url")

include(":modules:licenses:data")
include(":modules:licenses:di")
include(":modules:licenses:res")
include(":modules:licenses:ui")
include(":modules:licenses:vm")

include(":modules:navigation")

include(":modules:settings:keys")
include(":modules:settings:res")
include(":modules:settings:ui")

include(":modules:test:android")
include(":modules:test:http")
