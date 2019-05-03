package com.gadgetsfury.electionindia.kyc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_district_profile.*

class DistrictProfileFragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_district_profile, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eletcedCanidates = StringBuilder()
        for (s in resources.getStringArray(R.array.elected_candidates)) {
            eletcedCanidates.append(s)
            eletcedCanidates.append("\n\n")
        }

        val members = StringBuilder()
        for (s in resources.getStringArray(R.array.members)) {
            members.append(s)
            members.append("\n\n")
        }

        textViewElectedCandidates.text = eletcedCanidates.toString().trim()
        textViewMembers.text = members.toString().trim()

    }

}
