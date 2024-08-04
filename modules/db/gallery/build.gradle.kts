plugins {
  id("module-kotlin")
}

dependencies {
  api(projects.modules.gallery.model)

  implementation(libs.kotlinx.datetime)
}
