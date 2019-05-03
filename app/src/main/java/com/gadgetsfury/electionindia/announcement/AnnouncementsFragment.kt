package com.gadgetsfury.electionindia.announcement

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
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_announcements.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AnnouncementsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

   // private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_announcements, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 36, 36))
        val adapter = AnnouncementAdapter(activity!!)
        recyclerView.adapter = adapter

        val announcementDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).announcementDao()
        //progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = announcementDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                   // progressDialog.show()
                } else {
                    adapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(AnnouncementsViewModel::class.java)
        model.getAnnouncement().observe(this, Observer<List<Announcement>>{
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    announcementDao.clearAll()
                    announcementDao.insertAll(it)
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
