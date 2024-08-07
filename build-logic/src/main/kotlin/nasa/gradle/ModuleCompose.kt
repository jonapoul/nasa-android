package nasa.gradle

import com.android.build.gradle.LibraryPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
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
      apply(DependencyGuardPlugin::class.java)
    }

    extensions.configure<DependencyGuardPluginExtension> {
      configuration("releaseRuntimeClasspath")
    }

    tasks.withType<KotlinCompile> {
      compilerOptions {
        freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
      }
    }
  }
}
