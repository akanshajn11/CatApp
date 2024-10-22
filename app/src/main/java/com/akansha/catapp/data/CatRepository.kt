package com.akansha.catapp.data

import com.akansha.catapp.data.source.local.CatWithBreed
import kotlinx.coroutines.flow.Flow

interface CatRepository {

    suspend fun getCats(limit: Int, page: Int): List<CatWithBreed>

    suspend fun getCatDetails(id: String): CatWithBreed?

    fun observeAllFavouriteCatsWithBreed(): Flow<List<CatWithBreed>>

    fun observeAllFavoriteCatIds(): Flow<Set<String>>

    suspend fun deleteLocalStorage()

    suspend fun toggleCatFavourites(id: String)
}