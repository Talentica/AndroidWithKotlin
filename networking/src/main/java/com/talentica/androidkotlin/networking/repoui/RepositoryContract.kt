package com.talentica.androidkotlin.networking.repoui

import com.talentica.androidkotlin.networking.dto.Repository

/**
 * Created by kaushald on 13/06/17.
 */
interface RepositoryContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun onReposAvailable(repos: Array<Repository>)
        fun displayError(message: String)
        fun destroy()
    }

    interface Presenter {
        fun start(type: String, username: String)
        fun destroy()
    }

}