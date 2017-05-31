package com.talentica.androidkotlin.sqlitedatabase.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class DbHelper(context: Context) : SQLiteOpenHelper(context, "logs.db", null, 4) {
    val TAG = "DbHelper"
    val TABLE = "logs"

    companion object {
        val ID: String = "_id"
        val TIMESTAMP: String = "TIMESTAMP"
        val TEXT: String = "TEXT"
    }

    val DATABASE_CREATE =
        "CREATE TABLE if not exists " + TABLE + " (" +
        "${ID} integer PRIMARY KEY autoincrement," +
        "${TIMESTAMP} integer," +
        "${TEXT} text"+
        ")"

    fun log(text: String) {
        val values = ContentValues()
        values.put(TEXT, text)
        values.put(TIMESTAMP, Date().time)
        getWritableDatabase().insert(TABLE, null, values);
    }

    fun getLogs() : Cursor {
        return getReadableDatabase()
                .query(TABLE, arrayOf(ID, TIMESTAMP, TEXT), null, null, null, null, TIMESTAMP + " DESC");
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating: " + DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
    }

}
