package com.gadgetsfury.electionindia.sveep.video

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_playlist_item.view.*

class PlaylistAdapter(val context: Context): RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    private var list: List<Playlist>? = null
    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)

    fun setPlayList(list: List<Playlist>) {
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

        val playlist = list!![position]

        holder.textViewName.text = playlist.title
        glide.load(playlist.thumbnail).apply(requestOptions).into(holder.imageView)

        holder.itemView.setOnClickListener {

            it.findNavController().navigate(PlaylistFragmentDirections.actionPlaylistFragmentToVideosFragment(playlist))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewName = view.textViewName

    }

}