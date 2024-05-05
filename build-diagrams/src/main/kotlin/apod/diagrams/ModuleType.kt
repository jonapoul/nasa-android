package apod.diagrams

import apod.diagrams.ModuleType.AndroidApp
import apod.diagrams.ModuleType.AndroidCompose
import apod.diagrams.ModuleType.AndroidHilt
import apod.diagrams.ModuleType.AndroidLibrary
import apod.diagrams.ModuleType.AndroidViewModel
import apod.diagrams.ModuleType.Kotlin
import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project

internal enum class ModuleType(val string: String, val color: String) {
  AndroidApp(string = "Android App", color = "#5555FF"), // blue
  AndroidHilt(string = "Android Hilt", color = "#FF5555"), // red
  AndroidViewModel(string = "Android ViewModel", color = "#FCB103"), // orange
  AndroidCompose(string = "Android Compose", color = "#FFFF55"), // yellow
  AndroidLibrary(string = "Android Library", color = "#55FF55"), // green
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
}

internal fun Project.moduleType(): ModuleType = when {
  plugins.hasPlugin("com.android.application") -> AndroidApp
  plugins.hasPlugin("module-hilt") -> AndroidHilt
  plugins.hasPlugin("module-viewmodel") -> AndroidViewModel
  plugins.hasPlugin("module-compose") -> AndroidCompose
  plugins.hasPlugin("module-android") -> AndroidLibrary
  plugins.hasPlugin("module-kotlin") -> Kotlin
  else -> error("Unknown module type for ${project.path}")
}

internal fun Project.projColor(): Color = Color.rgb(moduleType().color).fill()
