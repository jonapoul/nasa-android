package nasa.gradle

import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPlugin
import com.dropbox.gradle.plugins.dependencyguard.DependencyGuardPluginExtension
import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ModuleViewModel : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionHilt::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
      apply(SortDependenciesPlugin::class.java)
      apply(DependencyGuardPlugin::class.java)
    }

    extensions.configure<DependencyGuardPluginExtension> {
      configuration("releaseRuntimeClasspath")
    }

    val api by configurations
    val implementation by configurations

    dependencies {
      api(libs.androidx.lifecycle.viewmodel.core)
      api(libs.dagger.core)
      api(libs.javaxInject)
      implementation(libs.hilt.android)
    }
  }
}
