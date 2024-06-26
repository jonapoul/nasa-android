@file:Suppress("HasPlatformType")

import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension
import com.vanniktech.dependency.graph.generator.ProjectDependencyGraphGeneratorTask
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.parse.Parser
import nasa.diagrams.CheckDotFileTask
import nasa.diagrams.GenerateGraphVizPngTask
import nasa.diagrams.ModuleType
import nasa.diagrams.TidyDotFileTask
import nasa.diagrams.getOutputFile
import nasa.diagrams.projColor

plugins {
  id("com.vanniktech.dependency.graph.generator")
}

val rows = ModuleType.values().map {
  "<TR><TD>${it.string}</TD><TD BGCOLOR=\"${it.color}\">module-name</TD></TR>"
}
val legend = Parser().read(
  """
    graph cluster_legend {
      label="Legend"
      node [style=filled, fillcolor="#bbbbbb"];
      Legend [shape=none, margin=0, label=<
        <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
          ${rows.joinToString(separator = "\n")}
        </TABLE>
      >];
    }
  """.trimIndent(),
)

val generator = DependencyGraphGeneratorExtension.ProjectGenerator(
  outputFormats = listOf(),
  projectNode = { node, proj -> node.add(proj.projColor()) },
  includeProject = { proj -> proj != proj.rootProject },
  graph = { graph ->
    graph.add(legend)
    graph.graphAttrs().add(Rank.sep(2.0))
    graph.nodeAttrs().add(Style.FILLED, Shape.BOX)
  },
)
val dotTask = tasks.register("generateModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) {
  group = "reporting"
  description = "Generates a project dependency graph for $path"
  projectGenerator = generator
  outputDirectory = projectDir
}

val tidyDotFileTask = tasks.register("tidyDotFileTask", TidyDotFileTask::class.java) {
  dotFile.set(dotTask.get().getOutputFile())
  dependsOn(dotTask)
}

val tempDotTask = tasks.register("tempModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) {
  group = JavaBasePlugin.VERIFICATION_GROUP
  projectGenerator = generator
  outputDirectory = project.layout.buildDirectory.file("diagrams-modules-temp").get().asFile
}

val tempTidyDotFileTask = tasks.register("tempTidyDotFileTask", TidyDotFileTask::class.java) {
  dotFile.set(tempDotTask.get().getOutputFile())
  dependsOn(tempDotTask)
}

val checkDotTask = tasks.register("checkModulesDotfile", CheckDotFileTask::class.java) {
  group = JavaBasePlugin.VERIFICATION_GROUP
  taskPath.set(dotTask.get().path)
  expectedDotFile.set(dotTask.get().getOutputFile())
  actualDotFile.set(tempDotTask.get().getOutputFile())
  dependsOn(tempTidyDotFileTask)
}

tasks.findByName("check")?.dependsOn(checkDotTask)

val modulePngTask = tasks.register("generateModulesPng", GenerateGraphVizPngTask::class.java) {
  reportDir.convention(layout.projectDirectory)
  dotFile.convention(reportDir.file("project-dependency-graph.dot"))
  pngFile.convention(reportDir.file("project-dependency-graph.png"))
  errorFile.convention(reportDir.file("project-dependency-error.log"))
  dependsOn(tidyDotFileTask)
}
