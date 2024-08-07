package nasa.gradle

import com.android.build.api.dsl.LibraryExtension
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ModuleResources : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionLicensee::class.java)
      apply(ConventionSpotless::class.java)
      apply(SortDependenciesPlugin::class.java)
      apply(DependencyGuardPlugin::class.java)
    }

    extensions.configure<DependencyGuardPluginExtension> {
      configuration("releaseRuntimeClasspath")
    }

    extensions.configure<LibraryExtension> {
      buildFeatures {
        androidResources = true
        resValues = true
      }
    }
  }
}
