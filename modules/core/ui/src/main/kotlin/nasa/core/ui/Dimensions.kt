package nasa.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nasa.core.res.R

object Dimensions {
  val Zero: Dp = 0.dp

  val Tiny: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_tiny)

  val Small: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_small)

  val Medium: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_medium)

  val Large: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_large)

  val VeryLarge: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_very_large)

  val Huge: Dp
    @ReadOnlyComposable
    @Composable
    get() = dimensionResource(R.dimen.padding_huge)
}
