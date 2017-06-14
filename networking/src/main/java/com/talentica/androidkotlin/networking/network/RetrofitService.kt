package com.talentica.androidkotlin.networking.network

import com.talentica.androidkotlin.networking.dto.Repository
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kaushald on 14/06/17.
 */
interface RetrofitService {

    @GET("users/{user}/repos?page=1&per_page=20")
    fun listRepos(@Path("user") username: String): retrofit2.Call<Array<Repository>>

}