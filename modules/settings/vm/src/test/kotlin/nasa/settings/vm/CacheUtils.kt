package nasa.settings.vm

internal fun ImageCache.writeBytesToFile(filename: String, numBytes: Int) {
  val bytes = ByteArray(numBytes) { (it % 8).toByte() }
  cacheDir.resolve(filename).outputStream().use { it.write(bytes) }
}
