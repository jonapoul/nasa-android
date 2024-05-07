// https://publicobject.com/2024/01/30/internal-visibility/
// Only used to access the "componentActivity" property, internal in Voyager
@file:Suppress(
  "CANNOT_OVERRIDE_INVISIBLE_MEMBER",
  "INVISIBLE_MEMBER",
  "INVISIBLE_REFERENCE",
)

package nasa.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.VoyagerHiltViewModelFactories
import cafe.adriel.voyager.hilt.internal.componentActivity

/**
 * This is an alternative to the [cafe.adriel.voyager.hilt.getViewModel] extension which comes with Voyager. We can't
 * use the original here because it gives the same ViewModel instance for any instance of the same screen type. So we
 * might have multiple ApodSingleScreens which load different ApodItems, but we want to store different ViewModels for
 * each screen instance.
 *
 * Only difference from the base is the use of the screen's hash code as another [remember] key, and also the same
 * hash used as a key for getting from the [ViewModelProvider].
 */
@Composable
inline fun <reified T : ViewModel> Screen.getViewModel(
  viewModelProviderFactory: ViewModelProvider.Factory? = null,
): T {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
    "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
  }

  val hash = hashCode()
  return remember(T::class, hash) {
    val hasDefaultViewModelProviderFactory = requireNotNull(lifecycleOwner as? HasDefaultViewModelProviderFactory) {
      "$lifecycleOwner is not a androidx.lifecycle.HasDefaultViewModelProviderFactory"
    }
    val viewModelStore = requireNotNull(viewModelStoreOwner.viewModelStore) {
      "$viewModelStoreOwner is null or have a null viewModelStore"
    }
    val factory = VoyagerHiltViewModelFactories.getVoyagerFactory(
      activity = context.componentActivity,
      delegateFactory = viewModelProviderFactory
        ?: hasDefaultViewModelProviderFactory.defaultViewModelProviderFactory,
    )
    val provider = ViewModelProvider(
      store = viewModelStore,
      factory = factory,
      defaultCreationExtras = hasDefaultViewModelProviderFactory.defaultViewModelCreationExtras,
    )
    provider.get(
      key = "${T::class.simpleName}-$hash",
      modelClass = T::class.java,
    )
  }
}
