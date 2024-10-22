package com.akansha.catapp.data.source.network.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import com.akansha.catapp.BuildConfig


class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("x-api-key", BuildConfig.CAT_API_KEY)
        return chain.proceed(request.build())
    }
}