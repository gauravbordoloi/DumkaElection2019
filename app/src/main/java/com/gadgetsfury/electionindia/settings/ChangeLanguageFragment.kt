package com.gadgetsfury.electionindia.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_change_language.*
import java.util.*

class ChangeLanguageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Locale.getDefault().displayLanguage == "हिन्दी") {
            btnHindi.isChecked = true
        } else {
            btnEnglish.isChecked = true
        }

        btnChangeLanguage.setOnClickListener {

            val i = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(i)

        }

    }

    override fun onResume() {
        if (radioGroup != null) {
            if (Locale.getDefault().displayLanguage == "हिन्दी") {
                btnHindi.isChecked = true
            } else {
                btnEnglish.isChecked = true
            }
        }
        super.onResume()
    }

}
