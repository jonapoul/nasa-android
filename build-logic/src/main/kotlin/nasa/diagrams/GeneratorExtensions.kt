package nasa.diagrams

import com.vanniktech.dependency.graph.generator.ProjectDependencyGraphGeneratorTask
import java.io.File

internal fun ProjectDependencyGraphGeneratorTask.getOutputFile(): File {
  return File(outputDirectory, "project-dependency-graph.dot")
}
