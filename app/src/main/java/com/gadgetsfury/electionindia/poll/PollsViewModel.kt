package com.gadgetsfury.electionindia.poll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PollsViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val pollingStations: MutableLiveData<List<PollingStation>> by lazy {
        MutableLiveData<List<PollingStation>>().also {
            loadPollingStations()
        }
    }

    fun getPollingStations(): LiveData<List<PollingStation>> {
        return pollingStations
    }

    private fun loadPollingStations() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getPolls(null, null)
            .enqueue(object: Callback<List<PollingStation>?> {
                override fun onFailure(call: Call<List<PollingStation>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    pollingStations.value = null
                }

                override fun onResponse(call: Call<List<PollingStation>?>, response: Response<List<PollingStation>?>) {
                    if (response.body() != null) {
                        pollingStations.value = response.body()
                        Log.e(TAG, response.body()!!.size.toString())
                    } else {
                        Log.e(TAG, "Body is Null")
                        pollingStations.value = null
                    }
                }
            })

    }

}