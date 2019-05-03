package com.gadgetsfury.electionindia.sveep.rohof

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RohofViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val rohof: MutableLiveData<List<ROHoF>> by lazy {
        MutableLiveData<List<ROHoF>>().also {
            loadROHoF()
        }
    }

    fun getROHoF(): LiveData<List<ROHoF>> {
        return rohof
    }

    private fun loadROHoF() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getRoHof(null, null)
            .enqueue(object : Callback<List<ROHoF>?> {
                override fun onFailure(call: Call<List<ROHoF>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    rohof.value = null
                }

                override fun onResponse(call: Call<List<ROHoF>?>, response: Response<List<ROHoF>?>) {
                    if (response.body() != null) {
                        rohof.value = response.body()
                    } else {
                        rohof.value = null
                        Log.e(TAG, "Response Null")
                    }
                }
            })

    }

}