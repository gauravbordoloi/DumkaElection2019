package com.gadgetsfury.electionindia.sveep.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_playlist.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class PlaylistFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

   // private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_playlist, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(LinearItemDecoration(18,18, 18, 18))
        val adapter = PlaylistAdapter(activity!!)
        recyclerView.adapter = adapter

        val playlistDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).playlistDao()
        //progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = playlistDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                } else {
                    adapter.setPlayList(list)
                }
            }
        }

        val model = ViewModelProviders.of(this).get(PlaylistViewModel::class.java)
        model.getPlaylist().observe(this, Observer<List<Playlist>> {
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {
                adapter.setPlayList(it)
                doAsync {
                    playlistDao.clearAll()
                    playlistDao.insertAll(it)
                }
            } else {
                Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
            }
        })

    }

}
