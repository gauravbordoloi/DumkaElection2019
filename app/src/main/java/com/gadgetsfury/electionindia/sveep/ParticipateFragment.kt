package com.gadgetsfury.electionindia.sveep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_participate.*

class ParticipateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_participate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnVfeed.setOnClickListener {
            findNavController().navigate(ParticipateFragmentDirections.actionParticipateFragmentToVFeedParticipateFragment())
        }

        btnVod.setOnClickListener {
            findNavController().navigate(ParticipateFragmentDirections.actionParticipateFragmentToVodPartcipateFragment())
        }

    }

}
