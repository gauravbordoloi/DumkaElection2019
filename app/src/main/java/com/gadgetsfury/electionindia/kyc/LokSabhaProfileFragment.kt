package com.gadgetsfury.electionindia.kyc

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_lok_sabha_profile.*

class LokSabhaProfileFragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_lok_sabha_profile, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mp = StringBuilder()
        mp.append(resources.getString(R.string.mp_parliamentary_constituency))
        mp.append("\n")
        mp.append(getString(R.string.name_of_sitting_mp))
        mp.append(resources.getString(R.string.sitting_mp))
        mp.append("\n")
        mp.append(getString(R.string.party))
        mp.append(resources.getString(R.string.mp_party))

        val ma = StringBuilder()
        var i = 0
        val sittingMLA = resources.getStringArray(R.array.sitting_mla)
        val maParty = resources.getStringArray(R.array.ma_party)
        for (s in resources.getStringArray(R.array.ma_assembly_constituency)) {
            ma.append(s)
            ma.append("\n")
            ma.append(getString(R.string.name_of_sitting_mla))
            ma.append(sittingMLA[i])
            ma.append("\n")
            mp.append(getString(R.string.party))
            ma.append(maParty[i])
            ma.append("\n\n\n")
            i += 1
        }

        textViewMP.text = mp.toString()
        textViewMA.text = ma.toString().trim()

    }

}
