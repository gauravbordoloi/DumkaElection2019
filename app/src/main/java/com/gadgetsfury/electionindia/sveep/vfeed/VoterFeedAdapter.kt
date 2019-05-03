package com.gadgetsfury.electionindia.sveep.vfeed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_voter_feeds_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class VoterFeedAdapter(val context: Context): RecyclerView.Adapter<VoterFeedAdapter.ViewHolder>(){

    private var list: List<VoterFeed>? = null
    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
    val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())


    fun setList(list: List<VoterFeed>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_voter_feeds_item, parent, false)
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

        val voterFeed = list!![position]

        holder.textViewName.text = voterFeed.name
        holder.textViewTime.text = sdf.format(Date(voterFeed.timestamp))
        glide.load(voterFeed.image).apply(requestOptions).into(holder.imageView)

        holder.itemView.setOnClickListener {

            it.findNavController().navigate(VoterFeedsFragmentDirections.actionVoterFeedsFragmentToVFeedDetailsFragment(voterFeed.id))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val textViewName = view.textViewName
        val textViewTime = view.textViewTime

    }

}