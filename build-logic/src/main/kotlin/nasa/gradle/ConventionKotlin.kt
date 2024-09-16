package nasa.gradle

import blueprint.core.javaVersion
import blueprint.core.javaVersionString
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ConventionKotlin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    tasks.withType<KotlinCompile> {
      compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(javaVersionString()))
        freeCompilerArgs.addAll(
          "-Xjvm-default=all-compatibility",
          "-opt-in=kotlin.RequiresOptIn",
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
      }
    }

    val javaVersion = javaVersion()
    extensions.configure<JavaPluginExtension> {
      sourceCompatibility = javaVersion
      targetCompatibility = javaVersion
    }

    val api by configurations
    dependencies {
      api(libs.kotlin.stdlib)
      api(libs.kotlinx.coroutines)
    }
  }
}
