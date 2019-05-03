package com.gadgetsfury.electionindia.announcement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_announcement_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class AnnouncementAdapter(val context: Context): RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {

    private var list: List<Announcement>? = null
    val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())

    fun setList(list: List<Announcement>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_announcement_item, parent, false)
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

        val announcement = list!![position]

        holder.textViewTitle.text = announcement.title
        holder.textViewTime.text = sdf.format(Date(announcement.timestamp))

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewTitle = view.textViewTitle
        val textViewTime = view.textViewTime

    }

}