@file:Suppress("unused")

package nasa.core.ui.preview

import kotlinx.collections.immutable.persistentListOf
import nasa.core.model.ThemeType

// https://angrytools.com/android/pixelcalc/ for OnePlus 6 with 2280x1080 in portrait
internal const val MY_PHONE_DPI = 384
const val MY_PHONE_WIDTH_DP = 450
internal const val MY_PHONE_HEIGHT_DP = 950

internal val ThemesToPreview = persistentListOf(
  ThemeType.Light,
  ThemeType.Dark,
  ThemeType.Midnight,
)
