package apod.core.http

import alakazam.kotlin.core.StateHolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadProgressStateHolder @Inject constructor() : StateHolder<DownloadState?>(initialState = null)
