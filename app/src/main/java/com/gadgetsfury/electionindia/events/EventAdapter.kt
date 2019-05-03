package com.gadgetsfury.electionindia.events

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.settings.ServerResponse
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.layout_event_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EventAdapter(val context: Context): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private val TAG = this.javaClass.simpleName

    private var list: List<ElectionEvent>? = null
    private val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())
    private val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
    private val eventDao = DatabaseCreator().getAppDatabase(context.applicationContext).eventDao()
    private val progressDialog = ProgressDialog(context)

    fun setList(list: List<ElectionEvent>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_event_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) {
            0
        } else {
            list!!.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = list!![position]

        holder.textViewTitle.text = event.name
        val time = context.getString(R.string.start_end_placeholder, sdf.format(event.startTimestamp), sdf.format(event.endTimestamp))
        holder.textViewTime.text = time

        Log.e(TAG, event.isInterested.toString())

        when {
            eventDao.isInterested(event.id) == 0 -> {
                //null
                holder.btnInterested.isEnabled = true
                holder.btnNotInterested.isEnabled = true
                holder.textViewInterested.text = context.getString(R.string.people_interested, event.interested)
            }
            eventDao.isInterested(event.id) == 1 -> {
                //interested
                holder.btnInterested.isEnabled = false
                holder.btnNotInterested.isEnabled = true
                holder.textViewInterested.text = context.getString(R.string.you_and_people_interested, event.interested)
            }
            else -> {
                holder.btnNotInterested.isEnabled = false
                holder.btnInterested.isEnabled = true
                holder.textViewInterested.text = context.getString(R.string.people_interested, event.interested)
                //not interested
            }
        }

        holder.btnNotInterested.setOnClickListener {

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
                        Toast.makeText(context, context.getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                        progressDialog.dismiss()
                        if (response.body() != null) {
                            if (response.body()!!.code == "success") {
                                holder.textViewInterested.text = context.getString(R.string.people_interested, event.interested)
                                event.isInterested = 2
                                eventDao.update(event)
                                holder.btnInterested.isEnabled = true
                                holder.btnNotInterested.isEnabled = false
                            } else {
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Null Response")
                        }
                    }
                })
        }

        holder.btnInterested.setOnClickListener {

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
                        Toast.makeText(context, context.getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                        progressDialog.dismiss()
                        if (response.body() != null) {
                            if (response.body()!!.code == "success") {
                                holder.textViewInterested.text = context.getString(R.string.you_and_people_interested, event.interested)
                                event.isInterested = 1
                                eventDao.update(event)
                                holder.btnInterested.isEnabled = false
                                holder.btnNotInterested.isEnabled = true
                            } else {
                                Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Null Response")
                        }
                    }
                })
        }

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(EventsFragmentDirections.actionEventsFragmentNavToEventDetailsFragment(event))
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewTitle = view.textViewTitle
        val textViewTime = view.textViewTime
        val btnNotInterested = view.btnNotInterested
        val btnInterested = view.btnInterested
        val textViewInterested = view.textViewInterested

    }

}