package com.gadgetsfury.electionindia.announcement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnnouncementsViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val announcements: MutableLiveData<List<Announcement>> by lazy {
        MutableLiveData<List<Announcement>>().also {
            loadAnnouncement()
        }
    }

    fun getAnnouncement(): LiveData<List<Announcement>> {
        return announcements
    }

    private fun loadAnnouncement() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getAnnouncements()
            .enqueue(object: Callback<List<Announcement>?> {
                override fun onFailure(call: Call<List<Announcement>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    announcements.value = null
                }

                override fun onResponse(call: Call<List<Announcement>?>, response: Response<List<Announcement>?>) {
                    if (response.body() != null) {
                        announcements.value = response.body()
                    } else {
                        Log.e(TAG, "Response is null")
                        announcements.value = null
                    }
                }
            })

    }

}