package nasa.core.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Returns a [FocusRequester] which shows the system keyboard after the given [keyboardDelay] period. Make sure to
 * pass this into the [androidx.compose.ui.focus.focusRequester] modifier on the TextField in question.
 */
@Composable
fun keyboardFocusRequester(keyboardDelay: Duration = KEYBOARD_SHOW_DELAY): FocusRequester {
  val focusRequester = remember { FocusRequester() }
  val keyboard = LocalSoftwareKeyboardController.current

  LaunchedEffect(focusRequester) {
    focusRequester.requestFocus()
    delay(keyboardDelay)
    keyboard?.show()
  }

  return focusRequester
}

private val KEYBOARD_SHOW_DELAY = 500.milliseconds
