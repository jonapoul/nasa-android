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
  companion object {
    val Empty = FilterConfig()
  }
}
