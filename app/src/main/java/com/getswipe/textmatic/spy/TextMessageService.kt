package com.getswipe.textmatic.spy

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.getswipe.textmatic.MainActivity
import com.getswipe.textmatic.MainApplication
import com.getswipe.textmatic.R
import com.getswipe.textmatic.data.ForwardingRuleDatabase
import timber.log.Timber

class TextMessageService : Service() {

    private var observer: TextMessageContentObserver? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        releaseObserver()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.v("Starting the service with start ID %d", startId)
        startForeground(NOTIFICATION_ID, createNotification())
        setUpObserver()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun releaseObserver() {
        if (observer != null) contentResolver.unregisterContentObserver(observer!!)
    }

    private fun setUpObserver() {
        if (observer != null) releaseObserver()
        val forwardingRules = MainApplication.CONTAINER[ForwardingRuleDatabase::class.java]
            .forwardingRuleDao()
            .getAll()
        observer = TextMessageContentObserver(
            this,
            forwardingRules.toTypedArray(),
            Handler(Looper.getMainLooper()!!))
        contentResolver.registerContentObserver(
            TextMessageContentObserver.URI, true, observer!!)
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannelGroup(
                NotificationChannelGroup(NOTIFICATION_CHANNEL_GROUP_ID, getString(R.string.notification_channel_label))
            )
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, getString(R.string.notification_channel_label),
                    NotificationManager.IMPORTANCE_MIN)
            notificationChannel.enableLights(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_service_title))
            .setContentText(getText(R.string.notification_service_content))
            .setSmallIcon(R.drawable.ic_baseline_compare_arrows_24)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.notification_service_content))
            .build()
    }

    companion object {

        private const val NOTIFICATION_CHANNEL_GROUP_ID = "default_group"
        private const val NOTIFICATION_CHANNEL_ID = "default_channel"
        private const val NOTIFICATION_ID = 60600

        fun startSelf(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, TextMessageService::class.java))
            } else {
                context.startService(Intent(context, TextMessageService::class.java))
            }
        }
    }
}
