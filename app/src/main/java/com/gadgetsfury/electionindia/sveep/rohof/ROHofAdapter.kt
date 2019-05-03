package com.gadgetsfury.electionindia.sveep.rohof

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.dialog.ImageViewDialog
import kotlinx.android.synthetic.main.layout_vod_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ROHofAdapter(val context: Context): RecyclerView.Adapter<ROHofAdapter.ViewHolder>() {

    private var list: List<ROHoF>? = null
    private val glide = Glide.with(context)
    private val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun setList(list: List<ROHoF>) {
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

        val rohof = list!![position]

        holder.textViewName.text = rohof.name
        holder.textViewDate.text = sdf.format(Date(rohof.timestamp))
        glide.load(rohof.image).into(holder.imageView)

        holder.itemView.setOnClickListener {
            ImageViewDialog(context, rohof.image).show()
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewName = view.textViewName
        val textViewDate = view.textViewDate

    }

}