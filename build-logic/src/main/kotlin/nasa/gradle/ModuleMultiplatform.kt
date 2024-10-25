package nasa.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class ModuleMultiplatform : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinMultiplatformPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionKover::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
//      apply(DependencyAnalysisPlugin::class.java) // doesn't support KMP
//      apply(SortDependenciesPlugin::class.java) // doesn't support KMP
    }

    extensions.configure<KotlinMultiplatformExtension> {
      jvm()
      androidTarget()
    }

    tasks.withType<KotlinCompile> {
      compilerOptions {
        freeCompilerArgs.addAll(COMPILER_ARGS)
      }
    }
  }
}
