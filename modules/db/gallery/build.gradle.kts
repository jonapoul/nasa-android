plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(libs.kotlinx.coroutines)
  api(projects.modules.gallery.model)
}
