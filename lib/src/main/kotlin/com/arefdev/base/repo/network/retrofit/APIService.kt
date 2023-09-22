package com.arefdev.base.repo.network.retrofit

import com.arefdev.base.repo.Constants
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET(Constants.URLs.API_TIME)
    fun getTime(@Path("timezone") timezone: String): Single<Response<JsonObject>>
}