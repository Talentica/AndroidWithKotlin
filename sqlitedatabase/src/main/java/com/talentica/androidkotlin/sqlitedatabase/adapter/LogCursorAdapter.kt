package com.talentica.androidkotlin.sqlitedatabase.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.talentica.androidkotlin.sqlitedatabase.R
import com.talentica.androidkotlin.sqlitedatabase.helpers.DbHelper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LogCursorAdapter(context: Context, cursor: Cursor) : CursorAdapter(context, cursor, true) {

    object DateHelper {
        const val DF_SIMPLE_STRING = "dd-MM-yy HH:mm"
        @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
            override fun initialValue(): DateFormat {
                return SimpleDateFormat(DF_SIMPLE_STRING, Locale.US)
            }
        }
    }


    override fun bindView(view: View, context: Context?, cursor: Cursor) {
        val tvText = view.findViewById<TextView>(R.id.tvText)
        tvText.setText(getString(cursor, DbHelper.TEXT))

        val tvTimestamp = view.findViewById<TextView>(R.id.tvTimestamp)
        val timeValue = getString(cursor, DbHelper.TIMESTAMP)
        val time = timeValue.toLong()
        val date = Date()
        date.time = time
        val dateText = DateHelper.DF_SIMPLE_FORMAT.get().format(date)
        tvTimestamp.setText(dateText)
    }

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(R.layout.item_log, parent, false);
    }

    private fun getString(cursor: Cursor, key: String): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(key))
    }
}
