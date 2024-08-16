package com.artemobrazumov.pincoder.service

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo.PRIORITY_HIGH
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import com.artemobrazumov.pincoder.screens.confirm.ConfirmActivity
import com.artemobrazumov.pincoder.R
import com.artemobrazumov.pincoder.screens.confirm.ConfirmActivity.Companion.PACKAGE_NAME
import com.artemobrazumov.pincoder.shared_preferences.SharedPreferencesRepository
import org.koin.android.ext.android.inject

class PinAccessibilityService: AccessibilityService() {

    private val sharedPreferencesRepository by inject<SharedPreferencesRepository>()
    private val packagesForPin
        get() = sharedPreferencesRepository.packagesId
    private var launcherList = listOf<String>()
    private val pinCodeExists
        get() = !sharedPreferencesRepository.pinCode.isNullOrEmpty()
    private val pinCodeCorrect
        get() = sharedPreferencesRepository.isCorrectPin == true
    private var lastLoggedInAppPackage: String? = null
    private var lastLoggedTime: Long = 0

    private val pincoderReceiver = PincoderBroadcastReceiver(
        onScreenOff = {
            clearSecuredAppCache()
        }
    )

    private fun clearSecuredAppCache() {
        sharedPreferencesRepository.securedAppCache = emptyList()
    }

    private fun processEvent(event: AccessibilityEvent) {
        if (!pinCodeCorrect && pinCodeExists &&
            isNotOpeningSamePackage(event) &&
            !sharedPreferencesRepository.securedAppCache.contains(event.packageName)
        ) {
            sharedPreferencesRepository.isCorrectPin = false
            lastLoggedInAppPackage = event.packageName.toString()
            lastLoggedTime = event.eventTime
            val intent = Intent(
                this@PinAccessibilityService,
                ConfirmActivity::class.java
            ).apply {
                setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_NO_ANIMATION
                            or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                )
                putExtra(PACKAGE_NAME, event.packageName)
            }
            startActivity(intent)
        }
    }

    private fun isNotOpeningSamePackage(event: AccessibilityEvent): Boolean =
        lastLoggedInAppPackage != event.packageName ||
            event.eventTime - lastLoggedTime >= APP_PINCODE_TIMEOUT

    override fun onCreate() {
        super.onCreate()
        startForegroundWithNotification()
        setLauncherList()
        registerReceiver(pincoderReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
    }

    private fun setLauncherList() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }
        launcherList = packageManager.queryIntentActivities(intent, 0).map {
            it.activityInfo.packageName
        }
    }

    private fun startForegroundWithNotification() {
        val channelId = createNotificationChannel()

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pincoder")
            .setContentText("Protecting")
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    private fun createNotificationChannel(): String {
        val channelId = "pincoder_app_service"
        val channelName = "Pincoder"
        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lightColor = android.graphics.Color.GREEN
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)

        return channelId
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event != null) {
            sharedPreferencesRepository.isCorrectPin = false
            if (packagesForPin.contains(event.packageName) || event.isMainActivityShown()) {
                processEvent(event)
            }
        }
    }

    private fun AccessibilityEvent.isMainActivityShown() =
        className?.contains(APP_CLASS_NAME) ?: false

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        unregisterReceiver(pincoderReceiver)
        super.onDestroy()
    }

    companion object {
        const val APP_CLASS_NAME = "com.artemobrazumov.pincoder.screens.main.MainActivity"

        const val APP_PINCODE_TIMEOUT = 1000L
    }
}