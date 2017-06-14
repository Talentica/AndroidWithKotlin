package com.talentica.androidkotlin.networking.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by kaushald on 14/06/17.
 */
class OkHttpService {

    companion object {
        fun httpGet(username: String, callback: Callback) {
            val fetchRepoUrl = "https://api.github.com/users/$username/repos?page=1&per_page=20"

            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(fetchRepoUrl)
                    .build()

            client.newCall(request).enqueue(callback)
        }
    }
}