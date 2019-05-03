package com.gadgetsfury.electionindia.kyc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_constituency.*

class ConstituencyFragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_constituency, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val constituencies = getString(R.string.constituencies_frag)
        textViewConstituencies.text = constituencies

        val assembly = StringBuilder()
        var i = 1
        for (s in resources.getStringArray(R.array.assembly_constituency)) {
            assembly.append(s)
            assembly.append("\n\n")
            i += 1
        }

        val parliamentary = StringBuilder()
        var j = 1
        for (s in resources.getStringArray(R.array.parliamentary_constituency)) {
            parliamentary.append(s)
            parliamentary.append("\n\n")
            j += 1
        }

        textViewAssembly.text = assembly.toString().trim()
        textViewParliamentary.text = parliamentary.toString().trim()

    }

}
