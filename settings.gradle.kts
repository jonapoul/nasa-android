@file:Suppress("UnstableApiUsage")

rootProject.name = "nasa-android"

pluginManagement {
  repositories {
    google {
      mavenContent {
        includeGroupByRegex(".*android.*")
        includeGroupByRegex(".*google.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google {
      mavenContent {
        includeGroupByRegex(".*android.*")
        includeGroupByRegex(".*google.*")
      }
    }
    mavenCentral()
    maven("https://jitpack.io")
  }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

include(":app")

include(":modules:about:data")
include(":modules:about:nav")
include(":modules:about:res")
include(":modules:about:ui")
include(":modules:about:vm")

include(":modules:apod:data:api")
include(":modules:apod:data:repo")
include(":modules:apod:grid:nav")
include(":modules:apod:grid:ui")
include(":modules:apod:grid:vm")
include(":modules:apod:model")
include(":modules:apod:nav")
include(":modules:apod:res")
include(":modules:apod:single:nav")
include(":modules:apod:single:ui")
include(":modules:apod:single:vm")

include(":modules:core:http")
include(":modules:core:model")
include(":modules:core:res")
include(":modules:core:ui")
include(":modules:core:url")

include(":modules:db:api")
include(":modules:db:apod")
include(":modules:db:gallery")
include(":modules:db:impl")

include(":modules:gallery:data:api")
include(":modules:gallery:data:repo")
include(":modules:gallery:image:ui")
include(":modules:gallery:image:vm")
include(":modules:gallery:model")
include(":modules:gallery:nav")
include(":modules:gallery:res")
include(":modules:gallery:search:ui")
include(":modules:gallery:search:vm")

include(":modules:home:nav")
include(":modules:home:res")
include(":modules:home:ui")
include(":modules:home:vm")

include(":modules:licenses:data")
include(":modules:licenses:nav")
include(":modules:licenses:res")
include(":modules:licenses:ui")
include(":modules:licenses:vm")

include(":modules:settings:nav")
include(":modules:settings:res")
include(":modules:settings:ui")
include(":modules:settings:vm")

include(":modules:test:http")
include(":modules:test:prefs")
include(":modules:test:resources")
