package com.akansha.catapp.catdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akansha.catapp.data.CatRepository
import com.akansha.catapp.data.source.local.CatWithBreed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatDetailUIState(
    val isLoading: Boolean = false,
    var catDetail: CatWithBreed? = null,
)

@HiltViewModel
class CatDetailViewModel @Inject constructor(
    private val catRepository: CatRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CatDetailUIState> =
        MutableStateFlow(CatDetailUIState(isLoading = true))
    val uiState: StateFlow<CatDetailUIState> = _uiState

    fun fetchCatDetails(id: String) {
        viewModelScope.launch {
            val catDetails = catRepository.getCatDetails(id)
            catDetails?.let { detail ->
                _uiState.update {
                    _uiState.value.copy(
                        isLoading = false,
                        catDetail = detail
                    )
                }
            }
        }
    }
}