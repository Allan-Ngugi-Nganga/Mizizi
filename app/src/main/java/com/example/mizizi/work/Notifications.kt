package com.example.mizizi.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/** Creates the notification channel the daily proverb job posts into. */
object Notifications {

    const val CHANNEL_ID = "daily_proverb"
    private const val CHANNEL_NAME = "Daily proverb"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "One proverb a day, with its meaning."
        }
        manager.createNotificationChannel(channel)
    }
}
