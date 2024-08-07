package nasa.gradle

import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ConventionAndroidLibrary : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(LibraryPlugin::class.java)
      apply(ConventionAndroidBase::class.java)
    }

    extensions.configure<LibraryExtension> {
      buildFeatures {
        // Force-disable useless build steps. These can be re-enabled on a per-module basis, if they need them
        androidResources = false
        dataBinding = false
        mlModelBinding = false
        prefabPublishing = false
      }

      packaging {
        resources.excludes.add("META-INF/*")
      }
    }
  }
}
