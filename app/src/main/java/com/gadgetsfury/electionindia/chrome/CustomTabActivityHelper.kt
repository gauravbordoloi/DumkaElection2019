package com.gadgetsfury.electionindia.chrome

import android.app.Activity
import android.content.ActivityNotFoundException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession

/**
 * Helper class to manage connection to the custom tab activity
 *
 * Adapted from the CustomTabActivityHelper in the sample code here:
 *
 * https://github.com/GoogleChrome/custom-tabs-client/blob/master/demos/src/main/java/org/chromium/customtabsdemos/CustomTabActivityHelper.java
 *
 * Created by segun.famisa on 04/06/2016.
 */

class CustomTabActivityHelper {

    /**
     * Interface to handle cases where the chrome custom tab cannot be opened.
     */
    interface CustomTabFallback {
        fun openUri(activity: Activity, uri: Uri)
    }

    companion object {

        private val TAG = this.javaClass.simpleName

        /**
         * Utility method for opening a custom tab
         *
         * @param activity Host activity
         * @param customTabsIntent custom tabs intent
         * @param uri uri to open
         * @param fallback fallback to handle case where custom tab cannot be opened
         */
        fun openCustomTab(
            activity: Activity, customTabsIntent: CustomTabsIntent,
            uri: Uri, fallback: CustomTabFallback?
        ) {
            val packageName = CustomTabsHelper.getPackageNameToUse(activity)

            if (packageName == null) {
                // no package name, means there's no chromium browser.
                // Trigger fallback
                fallback?.openUri(activity, uri)
            } else {
                // set package name to use
                customTabsIntent.intent.setPackage(packageName)
                try {
                    customTabsIntent.launchUrl(activity, uri)
                } catch (e: ActivityNotFoundException) {
                    Log.e(TAG, e.message)
                    e.printStackTrace()
                }
            }
        }
    }

}