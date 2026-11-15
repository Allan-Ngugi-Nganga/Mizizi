package com.example.mizizi.work

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mizizi.MainActivity
import com.example.mizizi.R
import com.example.mizizi.data.local.MiziziDatabase

/**
 * Runs once a day in the background and posts a proverb of the day.
 *
 * WorkManager is the right tool here because the reminder has to survive the
 * app being closed and the device rebooted, and because a daily notification is
 * exactly a deferrable periodic task: the system may delay it to save battery,
 * and that is fine for this feature.
 */
class DailyProverbWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val database = MiziziDatabase.getDatabase(context)
        val proverb = database.roomProverb() ?: return Result.success()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }

        Notifications.ensureChannel(context)

        val openApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, Notifications.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(proverb.original)
            .setContentText(proverb.meaning)
            .setStyle(NotificationCompat.BigTextStyle().bigText(proverb.meaning))
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        return Result.success()
    }

    private suspend fun MiziziDatabase.roomProverb() = proverbDao().random()

    private companion object {
        const val NOTIFICATION_ID = 1001
    }
}
