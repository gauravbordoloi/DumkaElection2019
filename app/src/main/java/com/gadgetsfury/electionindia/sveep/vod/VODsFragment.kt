package com.gadgetsfury.electionindia.sveep.vod

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_vods.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class VODsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    //private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_vods, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 36, 36))
        val adapter = VodAdapter(activity!!)
        recyclerView.adapter = adapter

        val vodDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).vodDao()
        //progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = vodDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                } else {
                    adapter.setList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(VodViewModel::class.java)
        model.getVODs().observe(this, Observer<List<VOD>> {
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    vodDao.clearAll()
                    vodDao.insertAll(it)
                }
            }
        })

        btnParticipate.setOnClickListener {

            findNavController().navigate(VODsFragmentDirections.actionVODsFragmentToVodPartcipateFragment())

        }

    }

    override fun onDestroy() {
        /*if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }*/
        super.onDestroy()
    }

}
