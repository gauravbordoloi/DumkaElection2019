package com.gadgetsfury.electionindia.sveep.vfeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_voter_feeds.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VoterFeedsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    //private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            Log.e(TAG, "going")
            rootView = inflater.inflate(R.layout.fragment_voter_feeds, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val adapter = VoterFeedAdapter(activity!!)
        recyclerView.adapter = adapter

        val voterFeedDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).voterFeedDao()
        //progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = voterFeedDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //Log.e(TAG, "1st")
                    //progressDialog.show()
                } else {
                    adapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(VfeedViewModel::class.java)
        model.getVfeeds().observe(this, Observer<List<VoterFeed>> {
            Log.e(TAG, "2nd")
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    voterFeedDao.clearAll()
                    voterFeedDao.insertAll(it)
                }
            }
        })

        btnParticipate.setOnClickListener {

            findNavController().navigate(VoterFeedsFragmentDirections.actionVoterFeedsFragmentToVFeedParticipateFragment())

        }

    }

    override fun onDestroy() {
        /*if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }*/
        super.onDestroy()
    }

}
