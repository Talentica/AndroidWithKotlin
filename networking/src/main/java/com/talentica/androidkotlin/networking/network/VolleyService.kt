package com.talentica.androidkotlin.networking.network

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

/**
 * Created by kaushald on 14/06/17.
 */
class VolleyService {

    companion object {

        fun httpGet(context: Context, username: String, callback: Response.Listener<JSONArray>,
                    errorCallback: Response.ErrorListener) {

            val queue = Volley.newRequestQueue(context)

            val fetchRepoUrl = "https://api.github.com/users/$username/repos?page=1&per_page=20"

            val jsonArrayRequest = JsonArrayRequest(fetchRepoUrl, callback, errorCallback)

            queue.add(jsonArrayRequest)
        }

    }

}