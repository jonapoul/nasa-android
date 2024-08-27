@file:Suppress("UnstableApiUsage", "MagicNumber")

package nasa.gradle

import blueprint.core.intProperty
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

class ConventionAndroidManagedDevices : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(ConventionAndroidBase::class.java)
    }

    extensions.getByType(CommonExtension::class).apply {
      testOptions {
        managedDevices {
          devices.apply {
            create<ManagedVirtualDevice>("pixel9") {
              device = "Pixel 9"
              apiLevel = intProperty(key = "blueprint.android.targetSdk")
              require64Bit = true
              systemImageSource = "aosp-atd" // "google", "google-atd", "aosp", or "aosp-atd"
            }

            create<ManagedVirtualDevice>("pixel3") {
              device = "Pixel 3"
              apiLevel = 30
              require64Bit = true
              systemImageSource = "aosp-atd" // "google", "google-atd", "aosp", or "aosp-atd"
            }
          }

          groups.apply {
            create("pixels") {
              targetDevices += devices.getByName("pixel3")
              targetDevices += devices.getByName("pixel9")
            }
          }
        }
      }
    }
  }
}
