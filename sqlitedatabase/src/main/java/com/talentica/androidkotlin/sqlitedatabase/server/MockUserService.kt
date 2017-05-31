package com.talentica.androidkotlin.sqlitedatabase.server

import com.google.gson.JsonObject

/**
 * Created by suyashg on 31/05/17.
 */
class MockUserService : com.talentica.androidkotlin.sqlitedatabase.server.UserService {

    private fun createOk(key: String? = null, value: String? = null) : com.google.gson.JsonObject {
        return create("ok", key, value)
    }

    private fun createError(key: String? = null, value: String? = null) : com.google.gson.JsonObject {
        return create("error", key, value)
    }

    fun isOk(jo: com.google.gson.JsonObject) : Boolean {
        return jo.get("status").asString == "ok"
    }

    private fun create(status: String, key: String?, value: String?) : com.google.gson.JsonObject {
        val result = com.google.gson.JsonObject()
        result.addProperty("status", status)
        if (key != null) {
            result.addProperty(key, value)
        }
        return result
    }

    override fun addFriend(user: com.talentica.androidkotlin.sqlitedatabase.model.User) : rx.Observable<JsonObject> {
        val result: com.google.gson.JsonObject
        if (user.id == "123") {
            result = createOk()
        } else {
            result = createError()
        }
        result.addProperty("name", user.name)
        result.addProperty("id", user.id)
        return rx.Observable.just(result)
    }

    override fun findUser(name: String) : rx.Observable<JsonObject> {
        val result: com.google.gson.JsonObject
        if (name == "alice" || name == "john") {
            result = createOk("id", if (name == "john") "123" else "456")
            result.addProperty("name", name)
        } else {
            result = createError()
        }
        Thread.sleep(1000)
        return rx.Observable.just(result)
    }
}