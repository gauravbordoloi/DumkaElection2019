package com.gadgetsfury.electionindia.sveep.vfeed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VfeedViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val vfeeds: MutableLiveData<List<VoterFeed>> by lazy {
        MutableLiveData<List<VoterFeed>>().also {
            loadVfeeds()
        }
    }

    fun getVfeeds(): LiveData<List<VoterFeed>> {
        return vfeeds
    }

    private fun loadVfeeds() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getVotersFeeds(null, null)
            .enqueue(object : Callback<List<VoterFeed>?> {
                override fun onFailure(call: Call<List<VoterFeed>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    vfeeds.value = null
                }

                override fun onResponse(call: Call<List<VoterFeed>?>, response: Response<List<VoterFeed>?>) {
                    if (response.body() != null) {
                        vfeeds.value = response.body()
                    } else {
                        vfeeds.value = null
                        Log.e(TAG, "Response Null")
                    }
                }
            })

    }

}