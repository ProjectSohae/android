package com.sohae.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources.NotFoundException
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification
import com.sohae.R
import com.sohae.activity.MainActivity


class MyFirebaseMessagingService: FirebaseMessagingService() {

    val tag = "sohae_fcm"

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(tag, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // TODO(developer): Handle FCM messages here.
        Log.d(tag, "From: ${message.from}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d(tag, "Message data payload: ${message.data}")

//            if (true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        message.notification?.let {
            sendNotification(it)
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

    }

    private fun sendNotification(
        notification: Notification
    ) {

        val intent = Intent(this, MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = notification.channelId ?: throw NotFoundException()
        val messageTitle = notification.title ?: "소해"
        val messageBody = notification.body ?: ""
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // api 26 이상 대응
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "테스트 알림"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            // 이미 존재하는 알림 채널인 경우, 생성하지 않는다.
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}