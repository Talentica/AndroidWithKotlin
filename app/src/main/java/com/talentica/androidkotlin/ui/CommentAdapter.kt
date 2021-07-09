/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.talentica.androidkotlin.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.talentica.androidkotlin.R
import com.talentica.androidkotlin.databinding.CommentItemBinding
import com.talentica.androidkotlin.db.entity.CommentEntity
import com.talentica.androidkotlin.ui.CommentAdapter.CommentViewHolder

class CommentAdapter(private val mCommentClickCallback: CommentClickCallback?) :
    RecyclerView.Adapter<CommentViewHolder>() {
    private var mCommentList: List<CommentEntity?>? = null
    fun setCommentList(comments: List<CommentEntity?>?) {
        if (mCommentList == null) {
            mCommentList = comments
            if (comments != null) {
                notifyItemRangeInserted(0, comments.size)
            }
        } else {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mCommentList!!.size
                }

                override fun getNewListSize(): Int {
                    return comments?.size ?: 0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mCommentList!![oldItemPosition]
                    val comment = comments?.get(newItemPosition)
                    return if (old != null && comment != null) {
                        old.id == comment.id
                    } else {
                        false
                    }
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val old = mCommentList!![oldItemPosition]
                    val comment = comments?.get(newItemPosition)
                    return if (old != null && comment != null) {
                        old.id == comment.id && old.postedAt === comment.postedAt && old.productId == comment.productId && old.text == comment.text
                    } else {
                        false
                    }
                }
            })
            mCommentList = comments
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding: CommentItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context), R.layout.comment_item,
                parent, false
            )
        binding.callback = mCommentClickCallback
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment = mCommentList!![position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mCommentList == null) 0 else mCommentList!!.size
    }

    class CommentViewHolder(val binding: CommentItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}