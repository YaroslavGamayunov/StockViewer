package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class StockSearchViewModel(private val repository: StockApiRepository) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Empty)

    val searchState = _searchState.asStateFlow()

    sealed class SearchState {
        data class OnQuery(val text: String) : SearchState()
        object Empty : SearchState()
    }

    fun search(query: String?): Flow<PagingData<StockItem>> {
        viewModelScope.launch {
            _searchState
                .emit(if (query.isNullOrEmpty()) SearchState.Empty else SearchState.OnQuery(query))
        }

        if (query.isNullOrEmpty()) {
            return flowOf(PagingData.empty())
        }

        return repository.search("%${query}%")
    }

    suspend fun popularTickers(n: Int): List<String> {
        return repository.popularTickers(n)
    }


}