package nasa.gradle

import nasa.diagrams.internal.depColour
import nasa.diagrams.internal.getOutputFile
import nasa.diagrams.internal.toNiceString
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.Generator
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorExtension.ProjectGenerator
import com.vanniktech.dependency.graph.generator.DependencyGraphGeneratorTask
import com.vanniktech.dependency.graph.generator.ProjectDependencyGraphGeneratorTask
import guru.nidi.graphviz.attribute.Font
import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.attribute.Rank
import guru.nidi.graphviz.attribute.Shape
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.model.Link
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.parse.Parser
import nasa.diagrams.CheckDotFileTask
import nasa.diagrams.DiagramsBlueprintExtension
import nasa.diagrams.GenerateGraphVizPngTask
import nasa.diagrams.TidyDotFileTask
import nasa.diagrams.color
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.TaskProvider

class ConventionDiagrams : Plugin<Project> {
  override fun apply(target: Project) {
    target.pluginManager.apply("com.vanniktech.dependency.graph.generator")
    val extension = target.extensions.create("diagrams", DiagramsBlueprintExtension::class.java)

    val modulePngTask = target.registerModuleTasks(extension)
    val dependenciesPngTask = target.registerDependencyTasks(extension)

    // Aggregate task
    target.tasks.register("generatePngs", Task::class.java) {
      dependsOn(modulePngTask)
      dependsOn(dependenciesPngTask)
    }
  }

  private fun Project.registerModuleTasks(
    extension: DiagramsBlueprintExtension,
  ): TaskProvider<GenerateGraphVizPngTask> {
    val projectGenerator = ProjectGenerator(
      outputFormats = listOf(),
      projectNode = { node, proj ->
        val moduleTypeFinder = extension.moduleTypeFinder.get()
        node.add(Shape.BOX).add(moduleTypeFinder.color(proj))
      },
      includeProject = { proj -> proj != proj.rootProject },
      graph = { graph ->
        if (extension.showLegend.get()) graph.addLegend(extension)
        graph.graphAttrs().add(
          Rank.sep(extension.rankSeparation.get()),
          Font.size(extension.nodeFontSize.get()),
          Rank.dir(extension.rankDir.get()),
        )
      },
    )
    val dotTask = tasks.register("generateModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) {
      group = "reporting"
      description = "Generates a project dependency graph for $path"
      this.projectGenerator = projectGenerator
      outputDirectory = projectDir
    }

    val tidyDotFileTask = tasks.register("tidyDotFile", TidyDotFileTask::class.java) {
      dotFile.set(dotTask.get().getOutputFile())
      toRemove.set(extension.removeModulePrefix)
      replacement.set(extension.replacementModulePrefix)
      dependsOn(dotTask)
    }

    val tempDotTask = tasks.register("tempModulesDotfile", ProjectDependencyGraphGeneratorTask::class.java) {
      group = JavaBasePlugin.VERIFICATION_GROUP
      this.projectGenerator = projectGenerator
      outputDirectory = project.layout.buildDirectory.file("diagrams-modules-temp").get().asFile
    }

    val tempTidyDotFileTask = tasks.register("tempTidyDotFile", TidyDotFileTask::class.java) {
      dotFile.set(tempDotTask.get().getOutputFile())
      toRemove.set(extension.removeModulePrefix)
      replacement.set(extension.replacementModulePrefix)
      dependsOn(tempDotTask)
    }

    tasks.register("checkModulesDotfile", CheckDotFileTask::class.java) {
      group = JavaBasePlugin.VERIFICATION_GROUP
      taskPath.set(dotTask.get().path)
      expectedDotFile.set(dotTask.get().getOutputFile())
      actualDotFile.set(tempDotTask.get().getOutputFile())
      dependsOn(tempTidyDotFileTask)
      if (extension.checkModulesDotfile.get()) {
        tasks.findByName("check")?.dependsOn(this)
      }
    }

    return tasks.register("generateModulesPng", GenerateGraphVizPngTask::class.java) {
      reportDir.convention(layout.projectDirectory)
      dotFile.convention(reportDir.file("project-dependency-graph.dot"))
      pngFile.convention(reportDir.file("project-dependency-graph.png"))
      errorFile.convention(reportDir.file("project-dependency-error.log"))
      dependsOn(tidyDotFileTask)
    }
  }

  private fun MutableGraph.addLegend(extension: DiagramsBlueprintExtension) {
    // Add the actual legend
    val legend = buildLegend(extension)
    add(legend)

    // Find the root module, probably ":app"
    val rootName = extension.topLevelProject.get()
    val rootNode = rootNodes()
      .filterNotNull()
      .distinct()
      .firstOrNull { it.name().toString() == rootName }
      ?: error("No node matching '$rootName'")

    // Add a link from the legend to the root module
    val link = Link.to(rootNode).with(Style.INVIS)
    add(legend.addLink(link))
  }

  private fun buildLegend(extension: DiagramsBlueprintExtension): MutableGraph {
    val rows = extension.moduleTypes.get().map { type ->
      "<TR><TD>${type.string}</TD><TD BGCOLOR=\"${type.color}\">module-name</TD></TR>"
    }
    return Parser().read(
      """
        graph cluster_legend {
          label="$LEGEND_LABEL"
          graph [fontsize=${extension.legendTitleFontSize.get()}]
          node [style=filled, fillcolor="${extension.legendBackground.get()}"];
          Legend [shape=none, margin=0, fontsize=${extension.legendFontSize.get()}, label=<
            <TABLE BORDER="0" CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">
              ${rows.joinToString(separator = "\n")}
            </TABLE>
          >];
        }
      """.trimIndent(),
    )
  }

  private fun Project.registerDependencyTasks(
    extension: DiagramsBlueprintExtension,
  ): TaskProvider<GenerateGraphVizPngTask> {
    val dependencyGenerator = Generator(
      outputFormats = listOf(),
      dependencyNode = { node, dep ->
        node
          .add(Shape.RECTANGLE)
          .add(dep.depColour())
          .add(Label.of(dep.toNiceString(target = this)))
      },
      graph = { graph ->
        graph
          .graphAttrs().add(Rank.sep(extension.rankSeparation.get()), Font.size(extension.nodeFontSize.get()))
          .nodeAttrs().add(Style.FILLED)
      },
    )

    val dotTask = tasks.register("generateDependenciesDotfile", DependencyGraphGeneratorTask::class.java) {
      group = "reporting"
      description = "Generates a dependency graph for $path"
      generator = dependencyGenerator
      outputDirectory = projectDir
    }

    val tempDotTask = tasks.register("tempDependenciesDotfile", DependencyGraphGeneratorTask::class.java) {
      group = JavaBasePlugin.VERIFICATION_GROUP
      generator = dependencyGenerator
      outputDirectory = project.layout.buildDirectory.file("diagrams-dependencies-temp").get().asFile
    }

    tasks.register("checkDependenciesDotfile", CheckDotFileTask::class.java) {
      group = JavaBasePlugin.VERIFICATION_GROUP
      taskPath.set(dotTask.get().path)
      expectedDotFile.set(dotTask.get().getOutputFile())
      actualDotFile.set(tempDotTask.get().getOutputFile())
      dependsOn(tempDotTask)
      if (extension.checkDependenciesDotfile.get()) {
        tasks.findByName("check")?.dependsOn(this)
      }
    }

    return tasks.register("generateDependenciesPng", GenerateGraphVizPngTask::class.java) {
      reportDir.convention(layout.projectDirectory)
      dotFile.convention(reportDir.file("dependency-graph.dot"))
      pngFile.convention(reportDir.file("dependency-graph.png"))
      errorFile.convention(reportDir.file("dependency-error.log"))
      dependsOn(dotTask)
    }
  }

  private companion object {
    const val LEGEND_LABEL = "Legend"
  }
}
