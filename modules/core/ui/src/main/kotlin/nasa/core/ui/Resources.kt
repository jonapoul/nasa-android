package nasa.core.ui

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun quantityResource(
  @PluralsRes id: Int,
  quantity: Int,
  vararg args: Any,
): String {
  val context = LocalContext.current
  val resources = remember(context) { context.resources }
  return remember(resources, quantity) { resources.getQuantityString(id, quantity, *args) }
}
