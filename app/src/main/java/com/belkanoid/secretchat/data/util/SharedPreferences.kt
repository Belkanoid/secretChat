package com.belkanoid.secretchat.data.util

import android.content.Context
import javax.inject.Inject


class SharedPreferences @Inject constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun putLong(name: String = USER_ID, value: Long) {
        editor.putLong(name, value)
        editor.apply()
    }

    fun getLong(name: String = USER_ID): Long {
        return sharedPreferences.getLong(name, -1)
    }

    companion object {
        private const val sharedName = "Shared Messenger"
        const val USER_ID = "userId"
    }
}