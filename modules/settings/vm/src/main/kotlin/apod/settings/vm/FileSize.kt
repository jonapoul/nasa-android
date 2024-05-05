package apod.settings.vm

import androidx.compose.runtime.Immutable

@Immutable
@JvmInline
value class FileSize private constructor(private val bytes: Long) : Comparable<FileSize> {
  constructor(bytes: Number) : this(bytes.toLong())

  override fun compareTo(other: FileSize): Int = bytes.compareTo(other.bytes)

  operator fun times(other: Number) = FileSize(bytes.toDouble() * other.toDouble())

  operator fun plus(other: FileSize) = FileSize(bytes + other.bytes)

  override fun toString(): String = when {
    this < KB -> "%d B".format(bytes)
    this < MB -> "%.1f kB".format(bytes.toDouble() / KB.bytes)
    this < GB -> "%.1f MB".format(bytes.toDouble() / MB.bytes)
    else -> "%.1f GB".format(bytes.toDouble() / GB.bytes)
  }

  companion object {
    val KB = FileSize(1_000)
    val MB = KB * 1e3
    val GB = MB * 1e3
  }
}

val Number.bytes: FileSize get() = FileSize(toLong())
val Number.kilobytes: FileSize get() = FileSize.KB * this
val Number.megabytes: FileSize get() = FileSize.MB * this
val Number.gigabytes: FileSize get() = FileSize.GB * this
