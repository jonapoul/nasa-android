package nasa.test.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import kotlin.coroutines.CoroutineContext

fun buildSharedPreferences(
  context: Context = ApplicationProvider.getApplicationContext(),
  name: String = "prefs",
): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

fun buildFlowSharedPreferences(
  coroutineContext: CoroutineContext,
  prefs: SharedPreferences = buildSharedPreferences(),
): FlowSharedPreferences = FlowSharedPreferences(prefs, coroutineContext)
