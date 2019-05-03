package com.gadgetsfury.electionindia.chrome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.gadgetsfury.electionindia.util.Util

class ChromeTabActionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.dataString

        if (data != null) {
            doWork(context, intent.getIntExtra(KEY_ACTION_SOURCE, -1), data)
        }
    }

    private fun doWork(context: Context, actionSource: Int, message: String) {

        when (actionSource) {
            ACTION_MENU_ITEM_WHATSAPP -> {
                Util.shareViaWhatsApp(context,message)
            }

            ACTION_MENU_ITEM_FACEBOOK -> {
                Util.shareViaFacebook(context,message)
            }

            ACTION_ACTION_BUTTON_SHARE -> {
                Util.shareText(context,message)
            }
        }

    }

    companion object {
        const val KEY_ACTION_SOURCE = "org.chromium.customtabsdemos.ACTION_SOURCE"
        const val ACTION_MENU_ITEM_WHATSAPP = 1
        const val ACTION_MENU_ITEM_FACEBOOK = 2
        const val ACTION_ACTION_BUTTON_SHARE = 3
    }

}