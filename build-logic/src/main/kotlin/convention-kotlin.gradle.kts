import blueprint.core.javaVersion
import blueprint.core.javaVersionString
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
  compilerOptions {
    jvmTarget.set(JvmTarget.fromTarget(javaVersionString()))
    freeCompilerArgs.addAll(
      "-Xjvm-default=all-compatibility",
      "-opt-in=kotlin.RequiresOptIn",
    )
  }
}

val javaVersion = javaVersion()
extensions.configure<JavaPluginExtension> {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}
