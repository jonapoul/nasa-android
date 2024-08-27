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
  AndroidApp(string = "Android App", color = "#FF5555"), // red
  AndroidHilt(string = "Android Hilt", color = "#FCB103"), // orange
  AndroidCompose(string = "Android Compose", color = "#FFFF55"), // yellow
  AndroidLibrary(string = "Android Library", color = "#55FF55"), // green
  AndroidResources(string = "Android Resources", color = "#00FFFF"), // cyan
  Kotlin(string = "Kotlin", color = "#A17EFF"), // purple
  ;

  object Finder : ModuleType.Finder {
    override fun find(project: Project): ModuleType = when {
      project.plugins.hasPlugin("com.android.application") -> AndroidApp
      project.plugins.hasPlugin("nasa.module.hilt") -> AndroidHilt
      project.plugins.hasPlugin("nasa.module.compose") -> AndroidCompose
      project.plugins.hasPlugin("nasa.module.android") -> AndroidLibrary
      project.plugins.hasPlugin("nasa.module.resources") -> AndroidResources
      project.plugins.hasPlugin("nasa.module.kotlin") -> Kotlin
      else -> error("Unknown module type for ${project.path}")
    }
  }
}
