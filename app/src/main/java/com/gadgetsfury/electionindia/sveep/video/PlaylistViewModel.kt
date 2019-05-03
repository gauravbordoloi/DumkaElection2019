package com.gadgetsfury.electionindia.sveep.video

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaylistViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val playlist: MutableLiveData<List<Playlist>> by lazy {
        MutableLiveData<List<Playlist>>().also {
            loadPlaylist()
        }
    }

    fun getPlaylist(): LiveData<List<Playlist>> {
        return playlist
    }

    private fun loadPlaylist() {

        VideoApiClient.getClient().create(VideoApiInterface::class.java)
            .getPlaylist()
            .enqueue(object : Callback<String?> {
                override fun onFailure(call: Call<String?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    playlist.value = null
                }

                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    if (response.body() != null) {

                        val items = JSONObject(response.body()).getJSONArray("items")
                        val list = mutableListOf<Playlist>()

                        for (i in 0 until items.length()) {
                            val id = items.getJSONObject(i).getString("id")
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

                            list.add(Playlist(id, title, description, thumbnail))
                        }
                        playlist.value = list

                    } else {
                        Log.e(TAG, "null response")
                        playlist.value = null
                    }
                }
            })

    }

}