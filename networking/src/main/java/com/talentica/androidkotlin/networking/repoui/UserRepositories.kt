package com.talentica.androidkotlin.networking.repoui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talentica.androidkotlin.networking.R
import com.talentica.androidkotlin.networking.dto.Repository

class UserRepositories : AppCompatActivity(), RepositoryContract.View {

    val progressBar: ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.progress_bar)
    }
    val repoList: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.rv_repo_list)
    }

    val mPresenter: RepositoryContract.Presenter by lazy {
        RepoPresenter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repositories)
        repoList.layoutManager = LinearLayoutManager(this)

        if (!intent.hasExtra("username")) {
            finish()
        }

        val username = intent.getStringExtra("username")
        val libType = intent.getStringExtra("libraryType")

        if (libType != null && username != null) {
            mPresenter.start(libType, username)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.destroy()
    }

    override fun onReposAvailable(repos: Array<Repository>) {
        val handler = Handler(Looper.getMainLooper())
        handler.post({
            val repoAdapter = RepoAdapter(repos)
            repoList.adapter = repoAdapter
        })
    }

    override fun showLoading() {
        val handler = Handler(Looper.getMainLooper())
        handler.post({
            progressBar.visibility = View.VISIBLE
            repoList.alpha = 0.4f
        })
    }

    override fun hideLoading() {
        val handler = Handler(Looper.getMainLooper())
        handler.post({
            progressBar.visibility = View.GONE
            repoList.alpha = 1.0f
        })
    }

    override fun displayError(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post({
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun destroy() {
        finish()
    }
}
