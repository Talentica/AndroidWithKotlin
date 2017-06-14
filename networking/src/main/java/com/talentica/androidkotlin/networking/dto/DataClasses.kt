package com.talentica.androidkotlin.networking.dto

/**
 * Created by kaushald on 13/06/17.
 */
data class RepoOwner(val login: String, val id: Long, val avatar_url: String, val url: String)

data class Repository(val id: Long, val name: String, val full_name: String, val private: Boolean,
                      val html_url: String, val description: String, val fork: Boolean, val
                      stargazers_count: Int, val clone_url: String, val watchers: Int, val owner:
                      RepoOwner, val updated_at: String)