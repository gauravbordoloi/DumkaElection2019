package com.gadgetsfury.electionindia.poll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import kotlinx.android.synthetic.main.fragment_poll_constituency.*

class PollConstituencyFragment : Fragment() {

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_poll_constituency, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(16,16,32,32))
        recyclerView.adapter = PollConstituencyAdapter(activity!!)

    }

}
