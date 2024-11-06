@file:Suppress("UnstableApiUsage")

package nasa.gradle

import blueprint.core.getLibrary
import blueprint.core.getVersion
import blueprint.recipes.androidBaseBlueprint
import blueprint.recipes.androidDesugaringBlueprint
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType

class ConventionAndroidBase : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    androidBaseBlueprint()

    androidDesugaringBlueprint(libs.getVersion("android.desugaring"))

    extensions.findByType(CommonExtension::class)?.apply {
      // e.g. "nasa.path.to.my.module"
      namespace = "nasa" + path.split(":").joinToString(".")

      defaultConfig {
        testInstrumentationRunner = "nasa.test.NasaHiltTestRunner"
      }

      testOptions {
        unitTests {
          // For Robolectric
          isIncludeAndroidResources = true
        }
      }
    }

    dependencies {
      add("implementation", libs.getLibrary("timber"))
    }
  }
}
