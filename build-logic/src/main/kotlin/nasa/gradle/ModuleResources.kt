package nasa.gradle

import com.android.build.api.dsl.LibraryExtension
import com.autonomousapps.DependencyAnalysisPlugin
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ModuleResources : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionLicensee::class.java)
      apply(ConventionSpotless::class.java)
      apply(DependencyAnalysisPlugin::class.java)
      apply(SortDependenciesPlugin::class.java)
    }

    extensions.configure<LibraryExtension> {
      buildFeatures {
        androidResources = true
        resValues = true
      }
    }
  }
}
