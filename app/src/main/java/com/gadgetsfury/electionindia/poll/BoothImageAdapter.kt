package com.gadgetsfury.electionindia.poll

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.dialog.ImageViewDialog
import kotlinx.android.synthetic.main.layout_image_item.view.*

class BoothImageAdapter(val context: Context): RecyclerView.Adapter<BoothImageAdapter.ViewHolder>() {

    private var devicewidth: Int = 0
    private var list: List<String>? = null
    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)

    fun setList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        devicewidth = displayMetrics.widthPixels * 2 / 5
        val view = LayoutInflater.from(context).inflate(R.layout.layout_image_item, parent, false)
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

        holder.itemView.layoutParams.width = devicewidth
        glide.load(list!![position]).apply(requestOptions).into(holder.imageView)

        holder.itemView.setOnClickListener {
            ImageViewDialog(context, list!![position]).show()
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView

    }

}