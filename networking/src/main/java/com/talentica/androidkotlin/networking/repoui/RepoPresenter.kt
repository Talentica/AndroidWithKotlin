package com.talentica.androidkotlin.networking.repoui

import android.app.Activity
import com.google.gson.Gson
import com.talentica.androidkotlin.networking.R
import com.talentica.androidkotlin.networking.dto.Repository
import com.talentica.androidkotlin.networking.network.OkHttpService
import com.talentica.androidkotlin.networking.network.RetrofitService
import com.talentica.androidkotlin.networking.network.VolleyService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * Created by kaushald on 14/06/17.
 */
class RepoPresenter(cxt: Activity, view: RepositoryContract.View) : RepositoryContract.Presenter {

    val context: Activity = cxt
    val view: RepositoryContract.View = view

    val gson = Gson()
    var isAlive = true

    init {

    }

    override fun start(type: String, username: String) {
        view.showLoading()
        when (type.toLowerCase()) {
            "okhttp" -> {
                loadWithOkHttp(username)
            }
            "volley" -> {
                loadWithVolley(username)
            }
            "retrofit" -> {
                loadWithRetrofit(username)
            }
            else -> {
                loadSampleDataFromAssets()
            }
        }
    }

    override fun destroy() {
        isAlive = false
    }

    private fun loadWithRetrofit(username: String) {
        val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        val retrofitCall = retrofitService.listRepos(username)

        retrofitCall.enqueue(object : Callback<Array<Repository>> {
            override fun onFailure(call: Call<Array<Repository>>?, t: Throwable?) {

                if (!isAlive) {
                    return
                }

                view.displayError(context.getString(R.string.some_error_occured))
                view.hideLoading()
                view.destroy()
            }

            override fun onResponse(call: Call<Array<Repository>>?, response:
            Response<Array<Repository>>?) {
                if (!isAlive) {
                    return
                }

                if (response?.body() != null) {
                    view.onReposAvailable(response.body()!!)
                    view.hideLoading()
                } else {
                    view.displayError(context.getString(R.string.some_error_occured))
                    view.hideLoading()
                    view.destroy()
                }
            }

        })
    }

    private fun loadWithVolley(username: String) {
        VolleyService.httpGet(context, username, com.android.volley.Response.Listener<JSONArray> {
            if (isAlive) {
                val repoList = gson.fromJson(it.toString(), Array<Repository>::class.java)
                view.onReposAvailable(repoList)
                view.hideLoading()
            }
        }, com.android.volley.Response.ErrorListener {
            if (isAlive) {
                view.displayError(context.getString(R.string.some_error_occured))
                view.hideLoading()
                view.destroy()
            }
        })
    }

    private fun loadWithOkHttp(username: String) {
        OkHttpService.httpGet(username, object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {

                if (!isAlive) {
                    return
                }

                view.displayError(context.getString(R.string.some_error_occured))
                view.hideLoading()
                view.destroy()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                if (!isAlive) {
                    return
                }

                if (response.body != null) {
                    val strResponse = response.body!!.string()
                    val repoList = gson.fromJson(strResponse, Array<Repository>::class.java)
                    view.onReposAvailable(repoList)
                    view.hideLoading()
                } else {
                    view.displayError(context.getString(R.string.some_error_occured))
                    view.hideLoading()
                    view.destroy()
                }
            }
        })
    }

    private fun loadSampleDataFromAssets() {
        val sampleJson: String = JSONObject(test.getTestData(context)).getString("repos")
        val repos = gson.fromJson(sampleJson, Array<Repository>::class.java)
        view.onReposAvailable(repos)
        view.hideLoading()
    }
}