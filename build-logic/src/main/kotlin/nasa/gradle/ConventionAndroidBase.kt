@file:Suppress("UnstableApiUsage")

package nasa.gradle

import blueprint.recipes.androidBaseBlueprint
import blueprint.recipes.androidDesugaringBlueprint
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

class ConventionAndroidBase : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    androidBaseBlueprint()
    androidDesugaringBlueprint(libs.versions.android.desugaring)

    extensions.findByType(CommonExtension::class)?.apply {
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

    val implementation by configurations
    dependencies {
      implementation(libs.timber)
    }
  }
}
