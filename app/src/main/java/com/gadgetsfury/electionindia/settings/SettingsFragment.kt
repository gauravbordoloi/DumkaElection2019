package com.gadgetsfury.electionindia.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.notification.NotificationUtil
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val feedsPref = findPreference<SwitchPreference>("feeds_notification")!!
        val announcementPref = findPreference<SwitchPreference>("announcements_notification")!!
        val eventsPref = findPreference<SwitchPreference>("events_notification")!!

        feedsPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            if (!feedsPref.isChecked) {
                NotificationUtil.subscribe(NotificationUtil.TOPIC_FEEDS)
                Toast.makeText(activity!!, getString(R.string.subscribed_to_notification), Toast.LENGTH_SHORT).show()
            } else {
                NotificationUtil.unSubscribe(NotificationUtil.TOPIC_FEEDS)
                Toast.makeText(activity!!, getString(R.string.unsubscribed_to_notification), Toast.LENGTH_SHORT).show()
            }
            return@OnPreferenceChangeListener true
        }

        announcementPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            if (!announcementPref.isChecked) {
                NotificationUtil.subscribe(NotificationUtil.TOPIC_FEEDS)
                Toast.makeText(activity!!, getString(R.string.subscribed_to_notification), Toast.LENGTH_SHORT).show()
            } else {
                NotificationUtil.unSubscribe(NotificationUtil.TOPIC_FEEDS)
                Toast.makeText(activity!!, getString(R.string.unsubscribed_to_notification), Toast.LENGTH_SHORT).show()
            }
            return@OnPreferenceChangeListener true
        }

        eventsPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            if (!eventsPref.isChecked) {
                NotificationUtil.subscribe(NotificationUtil.TOPIC_EVENTS)
                Toast.makeText(activity!!, getString(R.string.subscribed_to_notification), Toast.LENGTH_SHORT).show()
            } else {
                NotificationUtil.unSubscribe(NotificationUtil.TOPIC_EVENTS)
                Toast.makeText(activity!!, getString(R.string.unsubscribed_to_notification), Toast.LENGTH_SHORT).show()
            }
            return@OnPreferenceChangeListener true
        }

        findPreference<Preference>("version")!!.setOnPreferenceClickListener {
            Util.openLink(activity!!, Const.APP_LINK)
            return@setOnPreferenceClickListener false
        }

        findPreference<Preference>("developer")!!.setOnPreferenceClickListener {
            Util.openLink(activity!!, Const.DEVELOPER_LINK)
            true
        }

        findPreference<Preference>("language")!!.setOnPreferenceClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToChangeLanguageFragment())
            true
        }

    }

}