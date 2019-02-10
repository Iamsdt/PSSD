package com.iamsdt.pssd.ext

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

open class ScopeViewModel : ViewModel() {

    private val job = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancelChildren()
    }

}