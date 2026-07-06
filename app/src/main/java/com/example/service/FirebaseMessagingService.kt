package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.SupabaseManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FirebaseMessagingService : FirebaseMessagingService() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "Sale Fulbito", it.body ?: "Tienes una nueva notificación")
        }

        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        saveTokenLocally(token)
        scope.launch {
            SupabaseManager.syncDeviceToken(token)
        }
    }

    private fun saveTokenLocally(token: String) {
        val prefs: SharedPreferences = getSharedPreferences("sale_fulbito_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("fcm_token", token).apply()
    }

    companion object {
        fun getDeviceToken(context: Context): String? {
            val prefs = context.getSharedPreferences("sale_fulbito_prefs", Context.MODE_PRIVATE)
            return prefs.getString("fcm_token", null)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: "Sale Fulbito"
        val message = data["message"] ?: "Tienes una nueva notificación"
        val type = data["type"] ?: "general"

        when (type) {
            "booking_reminder" -> sendNotification(title, message, "booking_reminder")
            "coupon_available" -> sendNotification(title, message, "coupon_available")
            "nearby_court" -> sendNotification(title, message, "nearby_court")
            else -> sendNotification(title, message)
        }
    }

    private fun sendNotification(title: String, messageBody: String, type: String = "general") {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "sale_fulbito_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icono)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sale Fulbito Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de Sale Fulbito"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = when (type) {
            "booking_reminder" -> 1001
            "coupon_available" -> 1002
            "nearby_court" -> 1003
            else -> 1000
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
