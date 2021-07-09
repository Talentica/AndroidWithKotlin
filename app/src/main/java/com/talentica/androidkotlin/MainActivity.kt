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
package com.talentica.androidkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talentica.androidkotlin.model.Product

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            val fragment = ProductListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, ProductListFragment.Companion.TAG).commit()
        }
    }

    /** Shows the product detail fragment  */
    fun show(product: Product) {
        val productFragment: ProductFragment = ProductFragment.Companion.forProduct(product.id)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("product")
            .replace(
                R.id.fragment_container,
                productFragment, null
            ).commit()
    }
}