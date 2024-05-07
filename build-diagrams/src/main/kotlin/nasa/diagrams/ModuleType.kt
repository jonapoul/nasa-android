package nasa.diagrams

import guru.nidi.graphviz.attribute.Color
import nasa.diagrams.ModuleType.AndroidApp
import nasa.diagrams.ModuleType.AndroidCompose
import nasa.diagrams.ModuleType.AndroidHilt
import nasa.diagrams.ModuleType.AndroidLibrary
import nasa.diagrams.ModuleType.AndroidViewModel
import nasa.diagrams.ModuleType.Kotlin
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
