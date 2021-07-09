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
package com.talentica.androidkotlin.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.talentica.androidkotlin.db.entity.CommentEntity

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments where productId = :productId")
    fun loadComments(productId: Int): LiveData<List<CommentEntity?>?>?

    @Query("SELECT * FROM comments where productId = :productId")
    fun loadCommentsSync(productId: Int): List<CommentEntity?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<CommentEntity>?)
}