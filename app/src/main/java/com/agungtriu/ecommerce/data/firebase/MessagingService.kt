package com.agungtriu.ecommerce.data.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.core.room.entity.NotificationEntity
import com.agungtriu.ecommerce.data.MainRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var mainRepository: MainRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val notification = remoteMessage.toNotificationEntity()
            sendNotification(notification)

            CoroutineScope(Dispatchers.IO).launch {
                mainRepository.insertNotification(notification)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun sendNotification(notificationEntity: NotificationEntity) {
        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.app_navigation)
            .setDestination(R.id.notificationFragment)
            .createPendingIntent()

        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_active)
            .setContentTitle(notificationEntity.title)
            .setContentText(notificationEntity.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "notification channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = currentTimeMillis()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun currentTimeMillis(): Int {
        return (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    }

    private fun RemoteMessage.toNotificationEntity(): NotificationEntity {
        return NotificationEntity(
            title = this.data["title"],
            body = this.data["body"],
            image = this.data["image"],
            date = this.data["date"],
            time = this.data["time"],
            type = this.data["type"],
        )
    }
}