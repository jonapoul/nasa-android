package nasa.gallery.model

enum class MediaType(private val string: String) {
  Audio(string = "audio"),
  Image(string = "image"),
  Video(string = "video"),
  ;

  override fun toString(): String = string

  companion object {
    fun parse(string: String): MediaType = MediaType.entries
      .firstOrNull { it.toString() == string }
      ?: throw IllegalArgumentException("Invalid media type $string")
  }
}
