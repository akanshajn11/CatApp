package com.akansha.catapp.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface CatDao {

    @Query("SELECT * FROM cats ORDER BY id ASC limit:limit offset:offset")
    suspend fun get(offset: Int, limit: Int): List<CatWithBreed>

    @Query("SELECT * FROM cats WHERE id=:catId")
    suspend fun getById(catId: String): CatWithBreed?

    @Upsert
    suspend fun upsertAll(cats: List<Cat>)

    @Upsert
    suspend fun upsertAllBreeds(breed: List<Breed>)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_cats WHERE id = :catId)")
    suspend fun isFavouriteCat(catId: String): Boolean

    @Insert
    suspend fun addToFavourites(cat: FavouriteCat)

    @Delete
    suspend fun removeFromFavourites(cat: FavouriteCat)

    @Query("SELECT DISTINCT * from favourite_cats")
    fun observeAllFavouriteCatIds(): Flow<List<FavouriteCat>>

    @Query(
        """
        SELECT cats.* FROM cats 
        INNER JOIN favourite_cats ON cats.id = favourite_cats.id
    """
    )
    fun observeAllFavouriteCatsWithBreed(): Flow<List<CatWithBreed>>

    @Query("DELETE from favourite_cats")
    fun deleteFavouriteCats()

    @Query("DELETE from cats")
    fun deleteAllCats()

    @Query("DELETE from breeds")
    fun deleteAllBreeds()
}