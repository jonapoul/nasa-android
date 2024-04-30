import app.cash.licensee.UnusedAction

plugins {
  id("app.cash.licensee")
}

licensee {
  listOf(
    "Apache-2.0",
    "MIT",
    "EPL-1.0",
    "BSD-2-Clause",
  ).forEach { spdxId ->
    allow(spdxId)
  }

  listOf(
    "https://opensource.org/licenses/MIT", // Voyager
  ).forEach { url ->
    allowUrl(url)
  }

  unusedAction(UnusedAction.IGNORE)
}
