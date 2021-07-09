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
import com.talentica.androidkotlin.databinding.ProductItemBinding
import com.talentica.androidkotlin.db.entity.ProductEntity
import com.talentica.androidkotlin.ui.ProductAdapter.ProductViewHolder

class ProductAdapter(private val mProductClickCallback: ProductClickCallback?) :
    RecyclerView.Adapter<ProductViewHolder>() {
    var mProductList: List<ProductEntity?>? = null
    fun setProductList(productList: List<ProductEntity?>?) {
        if (mProductList == null) {
            mProductList = productList
            if (productList != null) {
                notifyItemRangeInserted(0, productList.size)
            }
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mProductList!!.size
                }

                override fun getNewListSize(): Int {
                    return productList?.size ?: 0
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mProductList!![oldItemPosition]
                    val new = productList?.get(newItemPosition)
                    return if (old != null && new != null) {
                        old.id == new.id
                    } else {
                        false
                    }
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val product = productList?.get(newItemPosition)
                    val old = productList?.get(oldItemPosition)
                    return if (product != null && old != null) {
                        (product.id == old.id && product.description == old.description
                                && product.name == old.name
                                && product.price == old.price)
                    } else {
                        false
                    }
                }
            })
            mProductList = productList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ProductItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context), R.layout.product_item,
                parent, false
            )
        binding.callback = mProductClickCallback
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.product = mProductList!![position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mProductList == null) 0 else mProductList!!.size
    }

    class ProductViewHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}