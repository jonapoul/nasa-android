package nasa.gradle

import com.squareup.sort.SortDependenciesPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ModuleAndroid : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionAndroidManagedDevices::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionStyle::class.java)
      apply(ConventionTest::class.java)
      apply(SortDependenciesPlugin::class.java)
    }
  }
}
