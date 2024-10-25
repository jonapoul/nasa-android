package nasa.gradle

import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class ConventionTestDependencies : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    val testImplementation = configurations.findByName("testImplementation")
    val isAndroid = project.plugins.any { it is AndroidBasePlugin }

    dependencies {
      testImplementation?.let { testImplementation ->
        testLibraries.forEach { lib -> testImplementation(lib) }
        testImplementation(project(":test:resources"))

        if (isAndroid) {
          testImplementation(libs.test.androidx.arch)
          testImplementation(libs.test.androidx.coreKtx)
          testImplementation(libs.test.androidx.junit)
          testImplementation(libs.test.androidx.rules)
          testImplementation(libs.test.androidx.runner)
          testImplementation(libs.test.mockk.android)
          testImplementation(libs.test.robolectric)
          testImplementation(libs.test.timber)

          val debugImplementation = configurations.getByName("debugImplementation")
          debugImplementation(libs.test.androidx.monitor)
          debugImplementation(project(":test:hilt"))

          val androidTestImplementation = configurations.getByName("androidTestImplementation")
          androidTestImplementation(libs.test.alakazam.core)
          androidTestImplementation(libs.test.androidx.arch)
          androidTestImplementation(libs.test.androidx.coreKtx)
          androidTestImplementation(libs.test.androidx.espresso.core)
          androidTestImplementation(libs.test.androidx.espresso.intents)
          androidTestImplementation(libs.test.androidx.junit)
          androidTestImplementation(libs.test.androidx.navigation)
          androidTestImplementation(libs.test.androidx.rules)
          androidTestImplementation(libs.test.androidx.runner)
          androidTestImplementation(libs.test.kotlin.common)
          androidTestImplementation(libs.test.kotlinx.coroutines)
          androidTestImplementation(libs.test.timber)
          androidTestImplementation(libs.test.turbine)
          androidTestImplementation(project(":test:resources"))
        }
      }
    }
  }
}
