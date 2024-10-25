package nasa.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider

internal val Project.testLibraries: List<Provider<MinimalExternalModuleDependency>>
  get() = listOf(
    libs.test.alakazam.core,
    libs.test.kotlin.common,
    libs.test.kotlinx.coroutines,
    libs.test.mockk.core,
    libs.test.okhttp,
    libs.test.turbine,
  )
