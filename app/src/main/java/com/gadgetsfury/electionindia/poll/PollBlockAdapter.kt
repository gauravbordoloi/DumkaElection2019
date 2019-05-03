package com.gadgetsfury.electionindia.poll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_text_view_item.view.*

class PollBlockAdapter(private val blocks: Array<String>, private val constituency: String): RecyclerView.Adapter<PollBlockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = blocks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = blocks[position]
        holder.itemView.setOnClickListener {

            it.findNavController().navigate(PollBlockFragmentDirections.actionPollBlockFragmentToPollingStationsFragment(blocks[position], constituency))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.textView

    }

}