plugins {
  id("convention-android-library")
  id("convention-style")
  id("convention-test")
  id("com.squareup.sort-dependencies")
  id("com.dropbox.dependency-guard")
}

dependencyGuard {
  configuration("releaseRuntimeClasspath")
}
