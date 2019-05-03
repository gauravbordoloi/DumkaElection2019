package com.gadgetsfury.electionindia.poll

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.GeoUtil
import com.gadgetsfury.electionindia.util.Util
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.layout_poll_station_item.view.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PollingStationAdapter(val context: Context): RecyclerView.Adapter<PollingStationAdapter.ViewHolder>(), Filterable {

    private val TAG = this.javaClass.simpleName

    private var list: List<PollingStation>? = null
    private var filteredList: List<PollingStation>? = null
    private val glide = Glide.with(context)
    private val simpleDateFormat = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)

    fun setList(list: List<PollingStation>) {
        this.list = list
        this.filteredList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_poll_station_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (filteredList.isNullOrEmpty()) {
            0
        } else {
            filteredList!!.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pollingStation = filteredList!![position]
        if (pollingStation.location != null) {
            holder.btnNavigate.isEnabled = true
            val latlng = LatLng(
                JSONObject(pollingStation.location).getDouble("lat"),
                JSONObject(pollingStation.location).getDouble("lng")
            )
            val s = GeoUtil.getMapImageLink(context, latlng, 13, 300, 100)
            //Log.e("TAG",s)
            glide.load(s).apply(requestOptions).into(holder.imageViewMap)

            holder.btnNavigate.setOnClickListener {
                Util.navigate(context, latlng)
            }
        } else {
            holder.btnNavigate.isEnabled = false
        }
        holder.textViewTitle.text = pollingStation.title
        //holder.textViewAddress.text = pollingStation.address

        holder.btnCall.setOnClickListener {
            if (pollingStation.phone == null) {
                return@setOnClickListener
            }

            Dexter.withActivity(context as Activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Util.call(context, pollingStation.phone)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(context, context.getString(R.string.permission_required_to_call), Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        holder.itemView.setOnClickListener {

            it.findNavController().navigate(PollingStationsFragmentDirections.actionPollingStationsFragmentToPollingStationDetailFragment(pollingStation))

        }

    }

    override fun getFilter(): Filter {

        return object: Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    filteredList = list
                } else {
                    val tempList = ArrayList<PollingStation>()
                    for (row in list!!) {

                        if (row.boothNumber.toLowerCase().contains(charString.toLowerCase()) /*|| row.pincode.toString().contains(charString)*/ || row.title.toLowerCase().contains(charString.toLowerCase())
                            /*row.address!!.toLowerCase().contains(charString.toLowerCase())*/) {
                            tempList.add(row)
                        }
                    }

                    filteredList = tempList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    filteredList = results.values as ArrayList<PollingStation>
                    notifyDataSetChanged()
                }
            }

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageViewMap = view.imageViewMap
        val textViewTitle = view.textViewTitle
        //val textViewAddress = view.textViewAddress
        val btnCall = view.btnCall
        val btnNavigate = view.btnNavigate

    }

}