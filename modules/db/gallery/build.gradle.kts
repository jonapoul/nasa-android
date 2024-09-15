plugins {
  id("nasa.module.kotlin")
}

dependencies {
  api(projects.gallery.model)

  implementation(libs.androidx.room.common)
}
