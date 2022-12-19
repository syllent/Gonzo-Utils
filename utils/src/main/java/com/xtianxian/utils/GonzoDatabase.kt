package com.xtianxian.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase

class GonzoDatabase(
    private val context: Context
) {

    private var dbHelper: GonzoDbHelper? = null
    private var database: SQLiteDatabase? = null

    @Throws(SQLException::class)
    fun open(): GonzoDatabase {
        dbHelper = GonzoDbHelper(context)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(gonzo: String, isLeader: Int) {
        val contentValue = ContentValues()
        contentValue.put(GonzoDbHelper.GONZO, gonzo)
        contentValue.put(GonzoDbHelper.IS_LEADER, isLeader)
        database!!.insert(
            GonzoDbHelper.TABLE_NAME,
            null,
            contentValue
        )
    }

    fun fetch(): Cursor? {
        val columns = arrayOf(
            GonzoDbHelper.ID,
            GonzoDbHelper.GONZO,
            GonzoDbHelper.IS_LEADER
        )
        val cursor = database!!.query(
            GonzoDbHelper.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }
}