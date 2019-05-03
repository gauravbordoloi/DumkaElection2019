package com.gadgetsfury.electionindia.sveep

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

class SveepAdapter(val context: Context): RecyclerView.Adapter<SveepAdapter.ViewHolder>() {

    val list = context.resources.getStringArray(R.array.sveep_items)
    val images = context.resources.obtainTypedArray(R.array.sveep_items_images)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = list[position]
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(images.getResourceId(position, 0),0,0,0)

        holder.itemView.setOnClickListener {

            when(position) {
                0 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToROHOFsFragment())
                1 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToVODsFragment())
                2 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToVoterFeedsFragment())
                3 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToParticipateFragment())
                4 -> it.findNavController().navigate(R.id.playlistFragment)
                5 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToFeedsFragment())
                6 -> it.findNavController().navigate(SveepFragmentDirections.actionSveepFragmentToEventsFragmentNav())
            }

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.textView

    }

}