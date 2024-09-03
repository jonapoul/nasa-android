package nasa.core.http.usage

import alakazam.kotlin.core.StateHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiUsageStateHolder @Inject constructor() : StateHolder<ApiUsage?>(initialState = null)
