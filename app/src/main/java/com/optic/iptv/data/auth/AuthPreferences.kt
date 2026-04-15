package com.optic.iptv.data.auth

import android.content.Context

object AuthPreferences {
    private const val PREFS = "optic_auth"
    private const val KEY_LOGGED_IN = "logged_in"

    fun isLoggedIn(context: Context): Boolean =
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_LOGGED_IN, false)

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_LOGGED_IN, loggedIn)
            .apply()
    }
}
