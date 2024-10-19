package nasa.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ConventionCoroutines : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    tasks.withType<KotlinCompile> {
      compilerOptions {
        freeCompilerArgs.addAll(
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=kotlinx.coroutines.FlowPreview",
        )
      }
    }
  }
}
