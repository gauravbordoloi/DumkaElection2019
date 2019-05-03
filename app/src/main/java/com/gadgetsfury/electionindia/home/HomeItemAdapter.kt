package com.gadgetsfury.electionindia.home

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.layout_home_item.view.*

class HomeItemAdapter(val context: Context): RecyclerView.Adapter<HomeItemAdapter.ViewHolder>() {

    private var devicewidth: Int = 0
    private val titles = context.resources.getStringArray(R.array.home_item_title)
    private val descriptions = context.resources.getStringArray(R.array.home_item_description)
    private val drawables = context.resources.obtainTypedArray(R.array.home_item_image)
    private val glide = Glide.with(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        devicewidth = displayMetrics.widthPixels * 2 / 5
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_home_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = titles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.layoutParams.width = devicewidth

        holder.textViewTitle.text = titles[position]
        holder.textViewDescription.text = descriptions[position]
        glide.load(drawables.getResourceId(position, 0)).into(holder.imageView)

        holder.itemView.setOnClickListener {

            when(position) {
                0 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPollConstituencyFragment())
                1 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSveepFragment())
                2 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAnnouncementsFragment())
                3 -> Util.openCustomChromeTab(context, Const.ELECTORAL_SEARCH)
                4 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVoterServicesFragment())
                //5 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFeedsFragment())
                5 -> it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToGamesFragment())
            }

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewTitle = view.textViewTitle
        val textViewDescription = view.textViewDescription
        val imageView = view.imageView

    }

}