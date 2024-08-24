package nasa.gradle

import com.android.build.gradle.LibraryPlugin
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ModuleCompose : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(LibraryPlugin::class.java)
      apply(ConventionCompose::class.java)
      apply(ConventionStyle::class.java)
      apply(SortDependenciesPlugin::class.java)
    }

    tasks.withType<KotlinCompile> {
      compilerOptions {
        freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
      }
    }
  }
}
