package com.talentica.androidkotlin.sqlitedatabase

import android.app.Application
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.talentica.androidkotlin.sqlitedatabase.helpers.DbHelper
import kotlin.properties.Delegates

class MainApplication : Application() {

    //database helper
    var dbHelper: DbHelper by Delegates.notNull()

    override fun onCreate() {
        //intantiate the db-helper when the app is created
        dbHelper = DbHelper(this)
    }
}
