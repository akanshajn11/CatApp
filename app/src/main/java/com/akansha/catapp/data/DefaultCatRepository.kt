package com.akansha.catapp.data

import com.akansha.catapp.data.source.local.Breed
import com.akansha.catapp.data.source.local.Cat
import com.akansha.catapp.data.source.local.CatDao
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.data.source.local.FavouriteCat
import com.akansha.catapp.data.source.network.NetworkDataSource
import com.akansha.catapp.di.DefaultDispatcher
import com.akansha.catapp.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map


@Singleton
class DefaultCatRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: CatDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CatRepository {

    override suspend fun getCats(limit: Int, page: Int): List<CatWithBreed> {
        return withContext(dispatcher) {
            val localCats = getLocalData(limit, page)
            if (localCats.isEmpty()) {
                val networkResult = networkDataSource.getCats(limit, page)
                val catWithBreedList = when (networkResult) {
                    is Result.Success -> networkResult.data
                    is Result.Error -> emptyList()
                }
                if (catWithBreedList.isEmpty()) {
                    return@withContext catWithBreedList
                } else {
                    val newBreeds =
                        catWithBreedList.filter { it.breed != null }.map { it.breed as Breed }
                    val newCats = catWithBreedList.map { it.cat }
                    updateLocalDataSource(newCats, newBreeds)
                    return@withContext getLocalData(limit, page)
                }
            } else {
                return@withContext localCats
            }
        }
    }

    private suspend fun getLocalData(limit: Int, page: Int): List<CatWithBreed> =
        localDataSource.get(offset = limit * (page - 1), limit = limit)

    private suspend fun updateLocalDataSource(networkCats: List<Cat>, networkBreeds: List<Breed>) {
        withContext(dispatcher) {
            localDataSource.upsertAllBreeds(networkBreeds)
            localDataSource.upsertAll(networkCats)
        }
    }

    override suspend fun getCatDetails(id: String): CatWithBreed? {
        return withContext(dispatcher) {
            return@withContext localDataSource.getById(id)
        }
    }

    override fun observeAllFavouriteCatsWithBreed(): Flow<List<CatWithBreed>> {
        return localDataSource.observeAllFavouriteCatsWithBreed()
    }

    override fun observeAllFavoriteCatIds(): Flow<Set<String>> {
        return localDataSource.observeAllFavouriteCatIds()
            .map { it.map { it.id }.toSet() }
    }

    override suspend fun deleteLocalStorage() {
        withContext(dispatcher) {
            localDataSource.deleteFavouriteCats()
            localDataSource.deleteAllCats()
            localDataSource.deleteAllBreeds()
        }
    }

    override suspend fun toggleCatFavourites(id: String) {
        withContext(dispatcher) {
            if (localDataSource.isFavouriteCat(id)) {
                localDataSource.removeFromFavourites(FavouriteCat(id))
            } else {
                localDataSource.addToFavourites(FavouriteCat(id))
            }
        }
    }
}