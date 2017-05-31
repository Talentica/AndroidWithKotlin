package com.talentica.androidkotlin.sqlitedatabase.server

import com.google.gson.JsonObject
import com.talentica.androidkotlin.sqlitedatabase.model.User
import rx.Observable

/**
 * Created by suyashg on 31/05/17.
 */
interface UserService {
    fun findUser(name: String) : Observable<JsonObject>
    fun addFriend(user: User) : Observable<JsonObject>
}
