plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.voyager.core)

  implementation(projects.gallery.model)
}
