package com.gadgetsfury.electionindia

import android.app.Application
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.gadgetsfury.electionindia.notification.NotificationUtil
import com.gadgetsfury.electionindia.util.SharedPref

class AppController : Application() {

    private val TAG = this.javaClass.simpleName

    companion object {

        private lateinit var robotoFont: Typeface

        fun getTypeface(): Typeface {
            return robotoFont
        }

    }

    override fun onCreate() {
        super.onCreate()

        if (SharedPref(this).isFirstTime()) {

            NotificationUtil.subscribeAll()

        }

        robotoFont = ResourcesCompat.getFont(applicationContext, R.font.roboto_font)!!

    }

}