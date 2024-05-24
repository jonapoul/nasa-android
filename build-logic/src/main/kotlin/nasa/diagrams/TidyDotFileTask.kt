package nasa.diagrams

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

open class TidyDotFileTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {
  @get:InputFile
  val dotFile: RegularFileProperty = objects.fileProperty()

  init {
    // never cache
    outputs.upToDateWhen { false }
  }

  @TaskAction
  fun invoke() {
    val dotFile = this.dotFile.get().asFile

    logger.info("Removing modules prefix from dotfile...")
    val dotFileContents = dotFile.readText()
    val contentsWithoutModulesPrefix = dotFileContents.replace(oldValue = ":modules:", newValue = ":")
    dotFile.writer().buffered().use { it.write(contentsWithoutModulesPrefix) }
  }
}
