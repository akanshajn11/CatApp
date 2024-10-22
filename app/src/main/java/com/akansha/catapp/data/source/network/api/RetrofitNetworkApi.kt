package com.akansha.catapp.data.source.network.api

import com.akansha.catapp.data.source.network.NetworkCat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitNetworkApi {
    @GET(value = "images/search")
    suspend fun getCats(
        @Query("has_breeds") hasBreeds: Boolean = true,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Response<List<NetworkCat>>
}