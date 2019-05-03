package com.gadgetsfury.electionindia.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.gadgetsfury.electionindia.R
import java.net.HttpURLConnection
import java.net.URL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.gadgetsfury.electionindia.SplashActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val TAG = this.javaClass.simpleName

    private val CHANNEL_ID = "election_india_channel"
    private val CHANNEL_NAME = "election_channel"

    private var bitmap: Bitmap? = null
    private var icon: Bitmap? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);

        if (remoteMessage!!.data.isNotEmpty()) {

            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            var image = ""
            if (remoteMessage.data.containsKey("image"))
                image = remoteMessage.data["image"]!!
           // Log.e(TAG, image)

            sendNotification(title!!, message!!, image)

        }

        if (remoteMessage.notification != null) {

        }

    }

    private fun sendNotification(title: String, message: String, image: String) {

        if (!image.isEmpty()) {
            bitmap = getBitmapFromUrl(image)
        }

        val resultIntent = Intent(applicationContext, SplashActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resultPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this)

        if (bitmap != null) {
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        builder.setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(icon)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(resultPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val id = System.currentTimeMillis().toInt()
        notificationManager.notify(id, builder.build())

    }

    /*
     *To get a Bitmap image from the URL received
     */
    fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", "" + e.message)
            return null
        }
    }

}