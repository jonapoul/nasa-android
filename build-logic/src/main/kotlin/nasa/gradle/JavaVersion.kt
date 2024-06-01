package nasa.gradle

import blueprint.core.intProperty
import blueprint.core.stringProperty
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

fun Project.javaVersionInt(): Int = intProperty(key = "nasa.javaVersion")

fun Project.javaVersionString(): String = stringProperty(key = "nasa.javaVersion")

fun Project.javaVersion(): JavaVersion = JavaVersion.toVersion(javaVersionInt())

fun Project.jvmTarget(): JvmTarget = JvmTarget.fromTarget(javaVersionString())
