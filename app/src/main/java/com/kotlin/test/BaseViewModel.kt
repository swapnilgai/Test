package com.kotlin.test

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus

sealed class UiState<out T>{
    data class Content<T>(val data: T, val isLoadingUI: Boolean = true): UiState<T>()
    data class Error(val message: String): UiState<Nothing>()
}

abstract class BaseViewModel<T>(initial: T):ViewModel() {

    val couroutineExceptionHandelr = CoroutineExceptionHandler { _, error ->

    }

    val baseViewModelScope = viewModelScope + couroutineExceptionHandelr

    private val _contentFlow = MutableStateFlow<UiState.Content<T>>(UiState.Content(initial))
    private val _errorFlow = MutableStateFlow<UiState.Error?>(null)


    val state = combine(_contentFlow, _errorFlow){ cont, error ->
        error ?: cont
    }.distinctUntilChanged().filterNotNull().stateIn(
        baseViewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _contentFlow.value
    )


    @MainThread
    protected fun setContent(data: T){
        _contentFlow.update {
            UiState.Content(data, isLoadingUI = false)
        }
    }

}