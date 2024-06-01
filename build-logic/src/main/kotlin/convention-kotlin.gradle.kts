import nasa.gradle.javaVersion
import nasa.gradle.jvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
  compilerOptions {
    jvmTarget.set(project.jvmTarget())
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
