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
import androidx.arch.core.util.Function
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.talentica.androidkotlin.db.DatabaseCreator
import com.talentica.androidkotlin.db.entity.ProductEntity


class ProductListViewModel(application: Application?) :
    AndroidViewModel(application!!) {
    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    val products: LiveData<List<ProductEntity?>?>?

    companion object {
        private val ABSENT: MutableLiveData<List<ProductEntity?>?> = MutableLiveData()
    }

    init {
        ABSENT.value = null
    }

    init {
        val databaseCreator = DatabaseCreator.getInstance(getApplication())
        val databaseCreated = databaseCreator!!.isDatabaseCreated
        products = Transformations.switchMap(databaseCreated) { isDbCreated ->
            (if (isDbCreated) {
                databaseCreator.database?.productDao()?.loadAllProducts()
            } else {
                ABSENT.value
            }) as LiveData<List<ProductEntity?>?>?
        }
        databaseCreator.createDb(getApplication())
    }
}