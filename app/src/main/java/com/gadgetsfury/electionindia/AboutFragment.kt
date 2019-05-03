package com.gadgetsfury.electionindia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewContributor1.setOnClickListener {
            Util.openCustomChromeTab(activity!!, getString(R.string.contributor_link_1))
        }

        textViewContributor2.setOnClickListener {
            Util.openCustomChromeTab(activity!!, getString(R.string.contributor_link_2))
        }

        textViewContributor3.setOnClickListener {
            Util.openCustomChromeTab(activity!!, getString(R.string.contributor_link_3))
        }

        textViewContributor4.setOnClickListener {
            Util.openCustomChromeTab(activity!!, getString(R.string.contributor_link_4))
        }

        textViewContributor5.setOnClickListener {
            Util.openCustomChromeTab(activity!!, getString(R.string.contributor_link_5))
        }

    }

}
