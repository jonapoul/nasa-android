package nasa.gradle

import blueprint.core.getLibrary
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider

internal val Project.testLibraries: List<Provider<MinimalExternalModuleDependency>>
  get() = listOf(
    libs.getLibrary("test.alakazam.core"),
    libs.getLibrary("test.kotlin.common"),
    libs.getLibrary("test.kotlinx.coroutines"),
    libs.getLibrary("test.mockk.core"),
    libs.getLibrary("test.okhttp"),
    libs.getLibrary("test.turbine"),
  )
