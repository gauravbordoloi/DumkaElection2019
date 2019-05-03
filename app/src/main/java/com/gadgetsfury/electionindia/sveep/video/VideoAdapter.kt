package com.gadgetsfury.electionindia.sveep.video

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_playlist_item.view.*

class VideoAdapter(val context: Context): RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    private var list: List<Video>? = null
    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)

    fun setVideos(list: List<Video>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_playlist_item, parent, false)
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

        val video = list!![position]

        holder.textViewName.text = video.title
        glide.load(video.thumbnail).apply(requestOptions).into(holder.imageView)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("video", video)
            context.startActivity(intent)

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewName = view.textViewName

    }

}