package com.gadgetsfury.electionindia.sveep.vod

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VodViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val vod: MutableLiveData<List<VOD>> by lazy {
        MutableLiveData<List<VOD>>().also {
            loadVod()
        }
    }

    fun getVODs(): LiveData<List<VOD>> {
        return vod
    }

    private fun loadVod() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getVods()
            .enqueue(object : Callback<List<VOD>?> {
                override fun onFailure(call: Call<List<VOD>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    vod.value = null
                }

                override fun onResponse(call: Call<List<VOD>?>, response: Response<List<VOD>?>) {
                    if (response.body() != null) {
                        vod.value = response.body()
                    } else {
                        vod.value = null
                        Log.e(TAG, "Response Null")
                    }
                }
            })

    }

}