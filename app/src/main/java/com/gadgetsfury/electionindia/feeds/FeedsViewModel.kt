package com.gadgetsfury.electionindia.feeds

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedsViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val feeds: MutableLiveData<List<Feed>> by lazy {
        MutableLiveData<List<Feed>>().also {
            loadFeeds()
        }
    }

    fun getFeeds(): LiveData<List<Feed>> {
        return feeds
    }

    private fun loadFeeds() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getFeeds()
            .enqueue(object: Callback<List<Feed>?> {
                override fun onFailure(call: Call<List<Feed>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    feeds.value = null
                }

                override fun onResponse(call: Call<List<Feed>?>, response: Response<List<Feed>?>) {
                    if (response.body() != null) {
                        feeds.value = response.body()
                    } else {
                        Log.e(TAG, "response is null")
                        feeds.value = null
                    }
                }
            })

    }

}