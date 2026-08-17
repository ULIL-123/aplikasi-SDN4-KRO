package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra("EXTRA_REMINDER_ID", 1L).toInt()
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "🔔 Pengingat Belajar SD 4 Kronggen"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Waktunya mempersiapkan jadwal pelajaran dan kebiasaan 7KAIH!"

        NotificationHelper.showPushNotification(
            context = context,
            title = title,
            message = message,
            notificationId = reminderId
        )
    }
}
