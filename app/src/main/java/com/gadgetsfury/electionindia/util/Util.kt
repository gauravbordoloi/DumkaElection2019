package com.gadgetsfury.electionindia.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.chrome.ChromeTabActionBroadcastReceiver
import com.gadgetsfury.electionindia.chrome.CustomTabActivityHelper
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Util {

    companion object {

        val TAG = Util::class.java.simpleName

        fun openCustomChromeTab(context: Context, link: String) {
            val intentBuilder = CustomTabsIntent.Builder()

            // set toolbar colors
            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.white))
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.white))

            // add menu items
            intentBuilder.addMenuItem(
                "Share to WhatsApp",
                createPendingIntent(context, ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_WHATSAPP)
            )
            intentBuilder.addMenuItem(
                "Share to Facebook",
                createPendingIntent(context, ChromeTabActionBroadcastReceiver.ACTION_MENU_ITEM_FACEBOOK)
            )

            // set action button
            intentBuilder.setActionButton(
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_share_png), "Share",
                createPendingIntent(context, ChromeTabActionBroadcastReceiver.ACTION_ACTION_BUTTON_SHARE)
            )

            // set start and exit animations
            intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            intentBuilder.setExitAnimations(
                context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )

            // build custom tabs intent
            val customTabsIntent = intentBuilder.build()

            // call helper to open custom tab
            CustomTabActivityHelper.openCustomTab(
                context as Activity,
                customTabsIntent,
                Uri.parse(link),
                object : CustomTabActivityHelper.CustomTabFallback {

                    override fun openUri(activity: Activity, uri: Uri) {
                        openLink(activity, uri.toString())
                    }

                })
        }

        private fun createPendingIntent(context: Context, actionSource: Int): PendingIntent {
            val actionIntent = Intent(context, ChromeTabActionBroadcastReceiver::class.java)
            actionIntent.putExtra(ChromeTabActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSource)
            return PendingIntent.getBroadcast(context, actionSource, actionIntent, 0)
        }

        fun openLink(context: Context, url: String) {
            try {
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(Intent.createChooser(i, "Choose one"))
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }

        fun shareText(context: Context, message: String) {
            try {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "BankersPoint")
                i.putExtra(Intent.EXTRA_TEXT, message)
                context.startActivity(Intent.createChooser(i, "choose one"))
            } catch (e: Exception) {
                Log.e("ERROR", "" + e.message)
            }

        }

        fun shareViaFacebook(context: Context, text: String) {
            try {
                val intent1 = Intent()
                intent1.setPackage("com.facebook.katana")
                intent1.action = "android.intent.action.SEND"
                intent1.type = "text/plain"
                intent1.putExtra("android.intent.extra.TEXT", text)
                context.startActivity(intent1)
            } catch (e: Exception) {
                val sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=$text"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl))
                context.startActivity(intent)
            }

        }

        fun shareViaWhatsApp(context: Context, text: String) {
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.type = "text/plain"
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.action = "android.intent.action.SEND"
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, text)
            try {
                context.startActivity(whatsappIntent)
            } catch (ex: android.content.ActivityNotFoundException) {
                ex.printStackTrace()
               Log.e(TAG, "WhatsApp not found")
            }

        }

        fun call(context: Context, phone: String) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }

        fun sendMailToSupport(context: Context) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(Const.DEVELOPER_EMAIl))
                putExtra(Intent.EXTRA_SUBJECT, "Contact from ${context.getString(R.string.app_name)} app")
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        fun navigate(context: Context, latLng: LatLng) {
            val intent = Intent(
                android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${latLng.latitude},${latLng.longitude}")
            )
            context.startActivity(intent)
        }

        fun shareApp(context: Context) {
            val shareText = context.getString(R.string.share_app_text, context.getString(R.string.app_name), Const.APP_LINK)
            shareImageAndText(context, BitmapFactory.decodeResource(context.resources, R.drawable.logo), "eci_dumka_logo", shareText)
        }

        private fun isExternalStorageReadOnly(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
        }

        private fun isExternalStorageAvailable(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == extStorageState
        }

        fun shareImageAndText(context: Context, bitmap: Bitmap, fileName: String, text: String) {

            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                Toast.makeText(context, context.getString(R.string.external_storage_not_available), Toast.LENGTH_SHORT).show()
                return
            }

            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val f = File(
                Environment.getExternalStorageDirectory().toString() + File.separator + fileName + ".jpg"
            )
            try {
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }

            share.putExtra(Intent.EXTRA_TEXT, text)
            share.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("file://" + Environment.getExternalStorageDirectory() + File.separator + fileName + ".jpg")
            )
            context.startActivity(Intent.createChooser(share, context.getString(R.string.choose_one)))

        }

    }

}