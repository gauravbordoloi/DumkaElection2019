package com.gadgetsfury.electionindia.feeds

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_news_feed_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class FeedsAdapter(val context: Context): RecyclerView.Adapter<FeedsAdapter.ViewHolder>() {

    private var list: List<Feed>? = null
    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
    val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())

    fun setList(list: List<Feed>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_feed_item, parent, false)
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

        val feed = list!![position]

        holder.textViewTitle.text = feed.title
        holder.textViewTime.text = sdf.format(Date(feed.timestamp))
        glide.load(feed.image).apply(requestOptions).into(holder.imageView)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("feed", feed)
            it.findNavController().navigate(R.id.newsFeedFragment, bundle)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewTitle = view.textViewTitle
        val textViewTime = view.textViewTime

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.clipToOutline = true
            }
        }

    }

}