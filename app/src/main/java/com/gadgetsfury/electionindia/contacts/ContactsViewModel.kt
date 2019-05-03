package com.gadgetsfury.electionindia.contacts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactsViewModel: ViewModel() {

    private val TAG = this.javaClass.simpleName

    private val contacts: MutableLiveData<List<Contact>> by lazy {
        MutableLiveData<List<Contact>>().also {
            loadContacts()
        }
    }

    fun getContacts(): LiveData<List<Contact>> {
        return contacts
    }

    private fun loadContacts() {

        ApiClient.getClient().create(ApiInterface::class.java)
            .getContacts()
            .enqueue(object: Callback<List<Contact>?> {
                override fun onFailure(call: Call<List<Contact>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    contacts.value = null
                }

                override fun onResponse(call: Call<List<Contact>?>, response: Response<List<Contact>?>) {
                    if (response.body() != null) {
                        contacts.value = response.body()
                    } else {
                        Log.e(TAG, "Null Data")
                        contacts.value = null
                    }
                }
            })

    }

}