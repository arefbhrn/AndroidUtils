package com.arefdev.base.repo.network.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Updated on 06/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.addHeader("Accept", "application/json")
//        requestBuilder.addHeader("Content-Type", "application/json");
        RetrofitApiClient.token?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
        return chain.proceed(requestBuilder.build())
    }
}