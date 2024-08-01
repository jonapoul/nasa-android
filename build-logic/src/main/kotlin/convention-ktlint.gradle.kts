import blueprint.recipes.ktlintBlueprint

ktlintBlueprint(
  ktlintCliVersion = libs.versions.ktlint.cli,
  ktlintComposeVersion = libs.versions.androidx.compose.lint.twitter,
)
