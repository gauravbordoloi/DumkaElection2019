package com.gadgetsfury.electionindia.events

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.settings.ServerResponse
import com.gadgetsfury.electionindia.util.Util
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_event_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = arguments!!.getParcelable<ElectionEvent>("event")!!
        Log.e(TAG, event.id.toString())
        val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())

        textViewTitle.text = event.name
        val time = getString(R.string.start_end_placeholder, sdf.format(event.startTimestamp), sdf.format(event.endTimestamp))
        textViewTime.text = time
        textViewDescription.text = event.description

        if (event.link.isNotEmpty()) {
            textViewLink.text = event.link
            textViewLink.paintFlags = textViewLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }

        val eventDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).eventDao()
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(activity!!)

        when {
            eventDao.isInterested(event.id) == 0 -> {
                //null
                btnInterested.isEnabled = true
                btnNotInterested.isEnabled = true
                textViewInterested.text = getString(R.string.people_interested, event.interested)
            }
            eventDao.isInterested(event.id) == 1 -> {
                //interested
                btnInterested.isEnabled = false
                btnNotInterested.isEnabled = true
                textViewInterested.text = getString(R.string.you_and_people_interested, event.interested)
            }
            else -> {
                //not interested
                btnNotInterested.isEnabled = false
                btnInterested.isEnabled = true
                textViewInterested.text = getString(R.string.people_interested, event.interested)
            }
        }

        textViewLink.setOnClickListener {
            Util.openCustomChromeTab(activity!!, event.link)
        }

        btnNotInterested.setOnClickListener {

            progressDialog.show()

            val map = HashMap<String, Int>()
            map["id"] = event.id
            when {
                eventDao.isInterested(event.id) == 0 -> {
                    //null
                    map["interested"] = 0
                    map["not_interested"] = 1
                }
                eventDao.isInterested(event.id) == 1 -> {
                    //interested
                    map["interested"] = -1
                    map["not_interested"] = 1
                }
                else -> {
                    map["interested"] = 0
                    map["not_interested"] = 0
                    //not interested
                }
            }
            apiInterface.setInterested(map)
                .enqueue(object: Callback<ServerResponse?> {
                    override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                        Log.e(TAG, t.message)
                        progressDialog.dismiss()
                        Toast.makeText(context, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                        progressDialog.dismiss()
                        if (response.body() != null) {
                            if (response.body()!!.code == "success") {
                                textViewInterested.text = getString(R.string.people_interested, event.interested)
                                event.isInterested = 2
                                eventDao.update(event)
                                btnInterested.isEnabled = true
                                btnNotInterested.isEnabled = false
                            } else {
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Null Response")
                        }
                    }
                })
        }

        btnInterested.setOnClickListener {

            progressDialog.show()

            val map = HashMap<String, Int>()
            map["id"] = event.id
            when {
                eventDao.isInterested(event.id) == 0 -> {
                    //null
                    map["interested"] = 1
                    map["not_interested"] = 0
                }
                eventDao.isInterested(event.id) == 1 -> {
                    //interested
                    map["interested"] = 0
                    map["not_interested"] = 0
                }
                else -> {
                    map["interested"] = 1
                    map["not_interested"] = -1
                    //not interested
                }
            }
            apiInterface.setInterested(map)
                .enqueue(object: Callback<ServerResponse?> {
                    override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                        Log.e(TAG, t.message)
                        progressDialog.dismiss()
                        Toast.makeText(context, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                        progressDialog.dismiss()
                        if (response.body() != null) {
                            if (response.body()!!.code == "success") {
                                textViewInterested.text = getString(R.string.you_and_people_interested, event.interested)
                                event.isInterested = 1
                                eventDao.update(event)
                                btnInterested.isEnabled = false
                                btnNotInterested.isEnabled = true
                            } else {
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e(TAG, "Null Response")
                            Toast.makeText(context, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

    }

}
