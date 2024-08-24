package nasa.home.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ApiUsageState {
  sealed interface HasDemoKey : ApiUsageState

  sealed interface NoUsage : ApiUsageState

  sealed interface HasUsage : ApiUsageState {
    val remaining: Int
    val upperLimit: Int
  }

  data object NoApiKey : ApiUsageState

  data object DemoKeyNoUsage : HasDemoKey, NoUsage

  data object RealKeyNoUsage : NoUsage

  data class DemoKeyHasUsage(
    override val remaining: Int,
    override val upperLimit: Int,
  ) : HasUsage, HasDemoKey

  data class RealKeyHasUsage(
    override val remaining: Int,
    override val upperLimit: Int,
  ) : HasUsage
}
