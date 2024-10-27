package nasa.gradle

import com.autonomousapps.DependencyAnalysisPlugin
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

class ModuleKotlin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KotlinPluginWrapper::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionKotlin::class.java)
      apply(ConventionKover::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
      apply("java-test-fixtures")
      apply(DependencyAnalysisPlugin::class.java)
      apply(SortDependenciesPlugin::class.java)
    }
  }
}
