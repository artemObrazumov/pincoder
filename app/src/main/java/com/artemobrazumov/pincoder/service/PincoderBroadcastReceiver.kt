package com.artemobrazumov.pincoder.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PincoderBroadcastReceiver(
    private val onScreenOff: () -> Unit
): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_OFF) {
            onScreenOff()
        }
    }
}