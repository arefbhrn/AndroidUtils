package com.arefdev.base.repo.network.retrofit

import com.google.gson.JsonElement
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Updated on 06/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
interface BaseApiService {

    @GET
    fun <T> generalGet(@Url url: String?): Single<Response<T>>

    @POST
    fun <T> generalPost(@Url url: String?, @Body body: JsonElement?): Single<Response<T>>
}