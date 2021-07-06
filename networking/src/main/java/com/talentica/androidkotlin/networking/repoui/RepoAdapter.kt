package com.talentica.androidkotlin.networking.repoui

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.talentica.androidkotlin.networking.R
import com.talentica.androidkotlin.networking.dto.Repository


/**
 * Created by kaushald on 13/06/17.
 */
class RepoAdapter(repos: Array<Repository>) : RecyclerView.Adapter<RepoAdapter.RepoVH>() {

    companion object {
        val TYPE_MINE = 0
        val TYPE_FORKED = 1
    }

    private val repos: Array<Repository> = repos

    override fun onBindViewHolder(holder: RepoVH, position: Int) {
        holder.bind(repos[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent,
                false)
        return RepoVH(viewType, itemView)

    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun getItemViewType(position: Int): Int {
        val repo = repos[position]
        return (if (repo.fork) TYPE_FORKED else TYPE_MINE)
    }

    class RepoVH(repoType: Int, itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val repoType = repoType

        private val repoForkedOrOriginal: ImageView by lazy {
            itemView.findViewById<ImageView>(R.id.img_github_avatar)
        }
        private val repoName by lazy {
            itemView.findViewById<TextView>(R.id.tv_repo_name)
        }
        private val repoDesc by lazy {
            itemView.findViewById<TextView>(R.id.tv_repo_desc)
        }
        private val repoStars by lazy {
            itemView.findViewById<TextView>(R.id.tv_repo_stars)
        }
        private val repoLastUpdated by lazy {
            itemView.findViewById<TextView>(R.id.tv_last_updated)
        }

        private val repoLayout by lazy {
            itemView.findViewById<RelativeLayout>(R.id.rl_repo_holder)
        }

        fun bind(repo: Repository) {
            repoName.text = repo.name
            repoDesc.text = repo.description
            repoStars.text = repo.stargazers_count.toString()
            repoLastUpdated.text = repo.updated_at
            repoLayout.background = if (repoType == TYPE_FORKED)
                itemView.context.getDrawable(R.drawable.circ_bg_other)
            else
                itemView.context.getDrawable(R.drawable.circ_bg_green)

            repoLayout.setOnClickListener {
                val url = repo.html_url
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                itemView.context.startActivity(i)
            }

            repoForkedOrOriginal.setImageResource(if (repoType == TYPE_FORKED) R.mipmap.ic_forked
            else R.mipmap.ic_mine)
        }
    }

}