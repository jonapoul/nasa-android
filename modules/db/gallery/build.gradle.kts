plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(projects.modules.gallery.model)

  implementation(libs.kotlinx.datetime)
}
