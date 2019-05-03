package com.gadgetsfury.electionindia.announcement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.candidate.Candidate
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CandidateViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val candidates: MutableLiveData<List<Candidate>> by lazy {
        MutableLiveData<List<Candidate>>().also {
            loadCandidates()
        }
    }

    fun getCandidates(): LiveData<List<Candidate>> {
        return candidates
    }

    private fun loadCandidates() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getCandidates()
            .enqueue(object: Callback<List<Candidate>?> {
                override fun onFailure(call: Call<List<Candidate>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    candidates.value = null
                }

                override fun onResponse(call: Call<List<Candidate>?>, response: Response<List<Candidate>?>) {
                    if (response.body() != null) {
                        candidates.value = response.body()
                    } else {
                        Log.e(TAG, "Response is null")
                        candidates.value = null
                    }
                }
            })

    }

}