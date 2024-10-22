package com.akansha.catapp.data.source.network

import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.util.Result


interface NetworkDataSource {
    suspend fun getCats(limit: Int, page: Int): Result<List<CatWithBreed>>
}