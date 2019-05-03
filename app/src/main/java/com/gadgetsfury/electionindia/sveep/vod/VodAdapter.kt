package com.gadgetsfury.electionindia.sveep.vod

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_vod_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class VodAdapter(val context: Context): RecyclerView.Adapter<VodAdapter.ViewHolder>() {

    private var list: List<VOD>? = null
    private val glide = Glide.with(context)
    private val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun setList(list: List<VOD>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_vod_item, parent, false)
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

        val vod = list!![position]

        holder.textViewName.text = vod.name
        glide.load(vod.image).into(holder.imageView)
        holder.textViewDate.text = sdf.format(Date(vod.timestamp))

        holder.itemView.setOnClickListener {

            it.findNavController().navigate(VODsFragmentDirections.actionVODsFragmentToVodDetailsFragment(vod.id))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewName = view.textViewName
        val textViewDate = view.textViewDate

    }

}