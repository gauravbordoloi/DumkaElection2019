package com.gadgetsfury.electionindia.kyc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.layout_text_view_item.view.*
import java.util.*

class KYCAdapter(val context: Context): RecyclerView.Adapter<KYCAdapter.ViewHolder>() {

    val list = context.resources.getStringArray(R.array.kyc_titles)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = list[position]
        holder.itemView.setOnClickListener {

            when(position) {

                0 -> it.findNavController().navigate(KYCFragmentDirections.actionKYCFragmentToConstituencyFragment())

                1 -> it.findNavController().navigate(KYCFragmentDirections.actionKYCFragmentToLokSabhaProfileFragment())

                2 -> it.findNavController().navigate(KYCFragmentDirections.actionKYCFragmentToDistrictProfileFragment())

                3 -> it.findNavController().navigate(KYCFragmentDirections.actionKYCFragmentToMapFragmentNav())

            }
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.textView

    }

}