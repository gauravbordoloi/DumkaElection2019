package com.gadgetsfury.electionindia.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val events: MutableLiveData<List<ElectionEvent>> by lazy {
        MutableLiveData<List<ElectionEvent>>().also {
            loadEvents()
        }
    }

    fun getEvents(): LiveData<List<ElectionEvent>> {
        return events
    }

    private fun loadEvents() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getEvents()
            .enqueue(object: Callback<List<ElectionEvent>?> {
                override fun onFailure(call: Call<List<ElectionEvent>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    events.value = null
                }

                override fun onResponse(call: Call<List<ElectionEvent>?>, response: Response<List<ElectionEvent>?>) {
                    if (response.body() != null) {
                        events.value = response.body()
                    } else {
                        Log.e(TAG, "Null Response")
                        events.value = null
                    }
                }
            })

    }

}