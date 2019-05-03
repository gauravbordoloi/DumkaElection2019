package com.gadgetsfury.electionindia.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class NotificationUtil {

    companion object {

        private val TAG = this.javaClass.simpleName

        const val TOPIC_FEEDS = "feeds"
        const val TOPIC_ANNOUNCEMENTS = "announcements"
        const val TOPIC_EVENTS = "events"
        const val TOPIC_OTHERS = "others"

        fun subscribeAll() {
            subscribe(TOPIC_FEEDS)
            subscribe(TOPIC_ANNOUNCEMENTS)
            subscribe(TOPIC_EVENTS)
            subscribe(TOPIC_OTHERS)
        }

        fun unSubscribeAll() {
            unSubscribe(TOPIC_FEEDS)
            unSubscribe(TOPIC_ANNOUNCEMENTS)
            unSubscribe(TOPIC_EVENTS)
            unSubscribe(TOPIC_OTHERS)
        }

        fun subscribe(topic: String) {
            FirebaseMessaging.getInstance()
                .subscribeToTopic(topic)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.e(TAG, it.exception!!.message)
                    }
                }
        }

        fun unSubscribe(topic: String) {
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic(topic)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.e(TAG, it.exception!!.message)
                    }
                }
        }

    }

}