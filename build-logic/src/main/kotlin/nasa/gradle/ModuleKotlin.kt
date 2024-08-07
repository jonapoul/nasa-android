package nasa.gradle

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

class ModuleKotlin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class.java)
      apply(ConventionKotlin::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
      apply(SortDependenciesPlugin::class.java)
      apply(DependencyGuardPlugin::class.java)
    }

    extensions.configure<DependencyGuardPluginExtension> {
      configuration("runtimeClasspath")
    }
  }
}
