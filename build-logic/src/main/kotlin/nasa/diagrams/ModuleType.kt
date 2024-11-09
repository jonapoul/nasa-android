package nasa.diagrams

import guru.nidi.graphviz.attribute.Color
import org.gradle.api.Project

interface ModuleType {
  val string: String
  val color: String

  fun interface Finder {
    fun find(project: Project): ModuleType
  }
}

fun ModuleType.Finder.color(project: Project): Color = Color.rgb(find(project).color).fill()

enum class NasaModuleType(override val string: String, override val color: String) : ModuleType {
  AndroidApp(string = "App", color = "#FF5555"), // red
  AndroidViewModel(string = "ViewModel", color = "#F5A6A6"), // pink
  AndroidHilt(string = "Hilt", color = "#FCB103"), // orange
  AndroidCompose(string = "Compose", color = "#FFFF55"), // yellow
  AndroidLibrary(string = "Android", color = "#55FF55"), // green
  AndroidResources(string = "Resources", color = "#00FFFF"), // cyan
  Navigation(string = "Navigation", color = "#5555FF"), // blue
  Multiplatform(string = "Multiplatform", color = "#9D8DF1"), // indigo
  Kotlin(string = "JVM", color = "#BB00FF"), // violet
  ;

  object Finder : ModuleType.Finder {
    override fun find(project: Project): ModuleType = when {
      project.plugins.hasPlugin("com.android.application") -> AndroidApp
      project.plugins.hasPlugin("nasa.module.hilt") -> AndroidHilt
      project.plugins.hasPlugin("nasa.module.viewmodel") -> AndroidViewModel
      project.plugins.hasPlugin("nasa.module.compose") -> AndroidCompose
      project.plugins.hasPlugin("nasa.module.android") -> AndroidLibrary
      project.plugins.hasPlugin("nasa.module.resources") -> AndroidResources
      project.plugins.hasPlugin("nasa.module.navigation") -> Navigation
      project.plugins.hasPlugin("nasa.module.multiplatform") -> Multiplatform
      project.plugins.hasPlugin("nasa.module.kotlin") -> Kotlin
      else -> error("Unknown module type for ${project.path}")
    }
  }
}
