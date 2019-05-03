package com.gadgetsfury.electionindia.sveep.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_videos.*
import kotlinx.android.synthetic.main.fragment_videos.recyclerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var progressDialog: ProgressDialog

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_videos, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlist = arguments!!.getParcelable<Playlist>("playlist")!!

        textViewName.text = playlist.title

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //recyclerView.layoutManager = GridLayoutManager(activity!!, 2)
        recyclerView.addItemDecoration(LinearItemDecoration(18,18, 18, 18))
        val adapter = VideoAdapter(activity!!)
        recyclerView.adapter = adapter

        val videoDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).videoDao()
        progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = videoDao.getAllByPlaylistId(playlist.id)
            uiThread {
                if (list.isNullOrEmpty()) {
                    progressDialog.show()
                } else {
                    adapter.setVideos(list)
                }
            }
        }

        VideoApiClient.getClient().create(VideoApiInterface::class.java)
            .getPlaylistVideos(playlist.id)
            .enqueue(object : Callback<String?> {
                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }

                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    if (response.body() != null) {

                        val items = JSONObject(response.body()).getJSONArray("items")
                        val list = mutableListOf<Video>()

                        for (i in 0 until items.length()) {
                            val id = items.getJSONObject(i).getJSONObject("snippet").getJSONObject("resourceId").getString("videoId")
                            val title = items.getJSONObject(i).getJSONObject("snippet").getString("title")
                            val description = items.getJSONObject(i).getJSONObject("snippet").getString("description")

                            val thumbnail = when {
                                !items.getJSONObject(i).getJSONObject("snippet").has("thumbnails") -> ""
                                items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").has("maxres") -> items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails")
                                    .getJSONObject("maxres").getString("url")
                                items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").has("standard") -> items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails")
                                    .getJSONObject("standard").getString("url")
                                items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").has("high") -> items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails")
                                    .getJSONObject("high").getString("url")
                                items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails").has("medium") -> items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails")
                                    .getJSONObject("medium").getString("url")
                                else -> items.getJSONObject(i).getJSONObject("snippet").getJSONObject("thumbnails")
                                    .getJSONObject("default").getString("url")
                            }

                            list.add(Video(id, title, description, thumbnail, playlist.id))
                        }
                        adapter.setVideos(list)

                        doAsync {
                            videoDao.clearAll(playlist.id)
                            videoDao.insertAll(list)
                        }

                    } else {
                        Log.e(TAG, "null response")
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }

}
