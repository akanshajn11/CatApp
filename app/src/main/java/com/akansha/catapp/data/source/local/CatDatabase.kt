package com.akansha.catapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Cat::class, Breed::class, FavouriteCat::class],
    version = 1,
    exportSchema = false,
)
abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
}