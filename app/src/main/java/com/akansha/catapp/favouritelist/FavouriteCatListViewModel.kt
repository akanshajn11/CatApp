package com.akansha.catapp.favouritelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akansha.catapp.data.CatRepository
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouriteCatListViewModel @Inject constructor(
    private val catRepository: CatRepository,
) : ViewModel() {

    val uiState: StateFlow<List<CatWithBreed>> = catRepository.observeAllFavouriteCatsWithBreed()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = emptyList(),
        )

    fun toggleFavoriteCat(catId: String) {
        viewModelScope.launch {
            catRepository.toggleCatFavourites(catId)
        }
    }
}