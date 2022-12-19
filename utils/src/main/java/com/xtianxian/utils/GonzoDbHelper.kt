package com.xtianxian.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GonzoDbHelper(
    context: Context
) : SQLiteOpenHelper(
    context,
    DB_NAME,
    null,
    DB_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        // Table Name
        const val TABLE_NAME = "GONZO_BATTLE"

        // Table columns
        const val ID = "_id"
        const val GONZO = "gonzo"
        const val IS_LEADER = "is_leader"

        // Database Information
        const val DB_NAME = "GONZO_BATTLE.DB"

        // database version
        const val DB_VERSION = 1

        // Creating table query
        private const val CREATE_TABLE = ("create table " + TABLE_NAME + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GONZO + " TEXT NOT NULL, " + IS_LEADER + " INT);")
    }
}