package com.akansha.catapp.catlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akansha.catapp.data.CatRepository
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class CatListUIState {
    object Loading : CatListUIState()
    object Empty : CatListUIState()
    data class Success(val cats: List<CatWithBreed>) : CatListUIState()
}

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val catRepository: CatRepository
) : ViewModel() {

    private var hasLoadedAllPages = false
    private val fetchLimit = 20

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage = _isLoadingNextPage.asStateFlow()

    private val _uiState: MutableStateFlow<CatListUIState> =
        MutableStateFlow(CatListUIState.Loading)
    val uiState: StateFlow<CatListUIState> = _uiState

    val favoriteCats: StateFlow<Set<String>> = catRepository.observeAllFavoriteCatIds()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptySet()
        )

    fun fetchNextPage() {
        if (_isLoadingNextPage.value || hasLoadedAllPages) {
            return
        }

        val currentCats = (_uiState.value as? CatListUIState.Success)?.cats.orEmpty()
        val nextPage = (currentCats.size / fetchLimit) + 1

        if (nextPage == 1) {
            _uiState.value = CatListUIState.Loading
        } else {
            _isLoadingNextPage.value = true
        }

        viewModelScope.launch {
            val newCats = catRepository.getCats(limit = fetchLimit, page = nextPage)
            val allCats = currentCats + newCats
            _isLoadingNextPage.value = false
            hasLoadedAllPages = newCats.isEmpty()
            if (allCats.isEmpty()) {
                _uiState.value = CatListUIState.Empty
            } else {
                _uiState.value = CatListUIState.Success(allCats)
            }
        }
    }

    fun toggleFavoriteCat(catId: String) {
        viewModelScope.launch {
            catRepository.toggleCatFavourites(catId)
        }
    }

    fun clearLocalStorage() {
        viewModelScope.launch {
            catRepository.deleteLocalStorage()
            _isLoadingNextPage.value = false
            _uiState.value = CatListUIState.Empty
            hasLoadedAllPages = false
            fetchNextPage()
        }
    }
}