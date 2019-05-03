package com.gadgetsfury.electionindia.poll

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.layout_text_view_item.view.*
import java.util.*

class PollConstituencyAdapter(val context: Context): RecyclerView.Adapter<PollConstituencyAdapter.ViewHolder>() {

    val list = context.resources.getStringArray(R.array.assembly_constituency)
    val names = context.resources.getStringArray(R.array.assembly_constituency_names)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = list[position]
        holder.itemView.setOnClickListener {

            val blockArr = when(position) {
                0 -> R.array.assembly_constituency_block_1
                1 -> R.array.assembly_constituency_block_2
                2 -> R.array.assembly_constituency_block_3
                3 -> R.array.assembly_constituency_block_4
                4 -> R.array.assembly_constituency_block_5
                5 -> R.array.assembly_constituency_block_6
                else -> 0
            }
            it.findNavController().navigate(PollConstituencyFragmentDirections.actionPollConstituencyFragmentToPollBlockFragment(blockArr, names[position]))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.textView

    }

}