package com.gadgetsfury.electionindia.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.feeds.Feed
import com.gadgetsfury.electionindia.feeds.FeedsAdapter
import com.gadgetsfury.electionindia.feeds.FeedsViewModel
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeFragment : Fragment() {

    private val TAG = this.javaClass.simpleName
   // private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFeed.setHasFixedSize(true)
        recyclerViewFeed.addItemDecoration(LinearItemDecoration(18,18, 36, 36))
        val feedsAdapter = FeedsAdapter(activity!!)
        recyclerViewFeed.adapter = feedsAdapter

        val feedsDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).feedDao()
       // progressDialog = ProgressDialog(activity!!)

        doAsync {
            val list = feedsDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                    Log.e(TAG, "showing")
                } else {
                    feedsAdapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(FeedsViewModel::class.java)
        model.getFeeds().observe(this, Observer<List<Feed>>{
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                feedsAdapter.setList(it)
                doAsync {
                    Log.e(TAG, "done")
                    feedsDao.clearAll()
                    feedsDao.insertAll(it)
                }
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 26, 10))
        recyclerView.adapter = HomeItemAdapter(activity!!)

    }

    override fun onDestroy() {
        /*if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }*/
        super.onDestroy()
    }

}
