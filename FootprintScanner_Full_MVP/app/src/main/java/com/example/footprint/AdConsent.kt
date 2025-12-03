package com.example.footprint

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences

object AdConsent {
    private const val PREF = "ad_consent"
    private const val KEY = "personalized"

    fun hasConsent(ctx: Context): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.contains(KEY)
    }

    fun requestConsent(ctx: Context, onDecision: (Boolean)->Unit) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle("Ad consent")
        builder.setMessage("Allow personalized ads to support the free app? You may choose non-personalized ads.")
        builder.setPositiveButton("Personalized") { _, _ ->
            ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putBoolean(KEY, true).apply()
            onDecision(true)
        }
        builder.setNegativeButton("Non-personalized") { _, _ ->
            ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putBoolean(KEY, false).apply()
            onDecision(false)
        }
        builder.setCancelable(false)
        builder.show()
    }

    fun isPersonalized(ctx: Context): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY, false)
    }
}
