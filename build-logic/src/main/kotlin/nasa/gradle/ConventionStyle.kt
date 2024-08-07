package nasa.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionStyle : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(ConventionDetekt::class.java)
      apply(ConventionKtlint::class.java)
      apply(ConventionLicensee::class.java)
      apply(ConventionSpotless::class.java)
    }
  }
}
