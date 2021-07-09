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
package com.talentica.androidkotlin.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.talentica.androidkotlin.db.DatabaseCreator
import com.talentica.androidkotlin.db.entity.CommentEntity
import com.talentica.androidkotlin.db.entity.ProductEntity

class ProductViewModel(
    application: Application,
    productId: Int
) : AndroidViewModel(application) {
    val observableProduct: LiveData<ProductEntity?>?
    @kotlin.jvm.JvmField
    var product = ObservableField<ProductEntity?>()
    private val mProductId: Int

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    val comments: LiveData<List<CommentEntity?>?>?
    fun setProduct(product: ProductEntity?) {
        this.product.set(product)
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    class Factory(private val mApplication: Application, private val mProductId: Int) :
        NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ProductViewModel(mApplication, mProductId) as T
        }
    }

    companion object {
        private val PRODUCT_ABSENT: MutableLiveData<ProductEntity?> = MutableLiveData()
        private val COMMENT_ABSENT: MutableLiveData<List<CommentEntity?>?> = MutableLiveData()
    }

    init {
        PRODUCT_ABSENT.value = null
        COMMENT_ABSENT.value = null
    }

    init {
        mProductId = productId
        val databaseCreator: DatabaseCreator? = DatabaseCreator.Companion.getInstance(
            getApplication<Application>()
        )
        comments = databaseCreator?.let {
            Transformations.switchMap(
                it.isDatabaseCreated
            ) { isDbCreated ->
                if (!isDbCreated!!) {
                    COMMENT_ABSENT
                } else {
                    databaseCreator.database?.commentDao()?.loadComments(mProductId)
                }
            }
        }
        observableProduct = databaseCreator?.let {
            Transformations.switchMap(
                it.isDatabaseCreated
            ) { isDbCreated ->
                if (!isDbCreated!!) {
                    PRODUCT_ABSENT
                } else {
                    databaseCreator.database?.productDao()?.loadProduct(mProductId)
                }
            }
        }
        databaseCreator?.createDb(getApplication())
    }
}