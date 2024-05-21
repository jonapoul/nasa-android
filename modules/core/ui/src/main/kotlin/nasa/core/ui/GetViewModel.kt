package nasa.core.ui

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
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

  val hash = remember(this) { hashCode() }
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

/**
 * Copied from [cafe.adriel.voyager.hilt.internal.findOwner], since it's internal in Voyager
 */
private inline fun <reified T> findOwner(context: Context): T? {
  var innerContext = context
  while (innerContext is ContextWrapper) {
    if (innerContext is T) {
      return innerContext
    }
    innerContext = innerContext.baseContext
  }
  return null
}

/**
 * Copied from [cafe.adriel.voyager.hilt.internal.componentActivity], since it's internal in Voyager
 */
val Context.componentActivity: ComponentActivity
  get() = findOwner<ComponentActivity>(this)
    ?: error("Context must be a androidx.activity.ComponentActivity. Current is $this")
