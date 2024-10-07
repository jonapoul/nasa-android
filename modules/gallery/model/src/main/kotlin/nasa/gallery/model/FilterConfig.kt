package nasa.gallery.model

data class FilterConfig(
  val query: String? = null,
  val center: Center? = null,
  val description: String? = null,
  val keywords: Keywords? = null,
  val location: String? = null,
  val mediaTypes: MediaTypes? = null,
  val nasaId: NasaId? = null,
  val photographer: Photographer? = null,
  val secondaryCreator: String? = null,
  val title: String? = null,
  val yearStart: Year? = null,
  val yearEnd: Year? = null,
) {
  val startYear: Year get() = yearStart ?: Year.Minimum
  val endYear: Year get() = yearEnd ?: Year.Maximum

  companion object {
    val Empty = FilterConfig()
  }
}
