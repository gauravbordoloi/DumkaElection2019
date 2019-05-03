package com.gadgetsfury.electionindia.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_feeds.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FeedsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    //private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_feeds, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFeed.setHasFixedSize(true)
        recyclerViewFeed.addItemDecoration(LinearItemDecoration(18,18, 36, 36))
        val adapter = FeedsAdapter(activity!!)
        recyclerViewFeed.adapter = adapter

        val feedsDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).feedDao()
        //progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = feedsDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                } else {
                    adapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(FeedsViewModel::class.java)
        model.getFeeds().observe(this, Observer<List<Feed>>{
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    feedsDao.clearAll()
                    feedsDao.insertAll(it)
                }
            }
        })

    }

    override fun onDestroy() {
        /*if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }*/
        super.onDestroy()
    }

}
