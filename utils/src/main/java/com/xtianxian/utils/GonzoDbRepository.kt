package com.xtianxian.utils

import android.content.Context

class GonzoDbRepository(
    private val context: Context
) {

    private val gonzoDatabase: GonzoDatabase = GonzoDatabase(context)

    init {
        gonzoDatabase.open()
    }

    fun close() {
        gonzoDatabase.close()
    }

    val exist: Boolean
        get() {
            gonzoDatabase.fetch()
                .use {
                    return it?.count != 0
                }
        }

    fun getGonzo(): String? {
        gonzoDatabase.fetch()
            .use {
                return if (it != null && it.count != 0) {
                    it.getString(1)
                } else {
                    null
                }
            }
    }

    fun getIsLeader(): Int? {
        gonzoDatabase.fetch()
            .use {
                return if (it != null && it.count != 0) {
                    it.getInt(2)
                } else {
                    null
                }
            }
    }

    fun add(gonzo: String, isLeader: Int) {
        gonzoDatabase.insert(gonzo, isLeader)
    }
}