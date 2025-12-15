package com.kotlin.test.feature

import androidx.compose.runtime.Immutable
import com.kotlin.test.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel { SearchViewModel(get()) }
}
@Immutable
data class SearchViewState(
    val response: List<Response> = listOf()
)

class SearchViewModel(val apiService: ApiService) : BaseViewModel<SearchViewState>(SearchViewState()){

    private val searchQuerySharedFlow =
        MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)

    private var searchJob: Job? = null
    init {
        baseViewModelScope.launch {
            searchQuerySharedFlow.distinctUntilChanged().debounce(200).map { searchQuery ->
                loadSearchData(searchQuery)
            }.collectLatest { result ->
                setContent(result)
            }
        }
    }


    fun loadData(input: String = ""){
        searchQuerySharedFlow.tryEmit(input)
    }

    private suspend fun loadSearchData(input: String = ""): SearchViewState {
           val result =  withContext(kotlinx.coroutines.Dispatchers.IO){
                val data = apiService.getData()
                val filtered = data.filter {
                    it.instrument_type.contains(input, ignoreCase = true)
                }
                SearchViewState(filtered)
            }
           return result
        }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

}