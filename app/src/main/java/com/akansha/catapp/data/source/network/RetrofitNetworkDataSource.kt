package com.akansha.catapp.data.source.network

import com.akansha.catapp.data.mapToCatWithBreed
import com.akansha.catapp.data.source.local.CatWithBreed
import com.akansha.catapp.data.source.network.api.RetrofitNetworkApi
import com.akansha.catapp.util.Result
import timber.log.Timber
import javax.inject.Inject


class RetrofitNetworkDataSource @Inject constructor(
    private val networkApi: RetrofitNetworkApi
) : NetworkDataSource {
    override suspend fun getCats(
        limit: Int,
        page: Int
    ): Result<List<CatWithBreed>> {
        return try {
            val response = networkApi.getCats(limit = limit, page = page)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.mapToCatWithBreed())
            } else {
                val throwable = Throwable(
                    "There was an issue fetching cats: ${
                        response.errorBody().toString()
                    }"
                )
                Timber.e(throwable)
                Result.Error(throwable)
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.Error(exception)
        }
    }
}