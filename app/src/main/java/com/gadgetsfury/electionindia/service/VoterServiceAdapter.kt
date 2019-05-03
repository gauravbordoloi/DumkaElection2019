package com.gadgetsfury.electionindia.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.layout_text_view_item.view.*
import java.util.*

class VoterServiceAdapter(val context: Context): RecyclerView.Adapter<VoterServiceAdapter.ViewHolder>() {

    val list = context.resources.getStringArray(R.array.voter_services)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_text_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = list[position]
        holder.itemView.setOnClickListener {

            var isHindi = false
            if (Locale.getDefault().displayLanguage == "हिन्दी") {
                isHindi = true
            }

            when(position) {

                0,1 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.APPLY_ONLINE_OR_TRANSFER_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.APPLY_ONLINE_OR_TRANSFER_ENGLISH)
                }

                2 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.DELETION_OR_OBJECTION_ELECTORAL_ROLL_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.DELETION_OR_OBJECTION_ELECTORAL_ROLL_ENGLISH)
                }

                3 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.CORRECTION_OF_ENTRIES_ELECTORAL_ROLL_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.CORRECTION_OF_ENTRIES_ELECTORAL_ROLL_ENGLISH)
                }

                4 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.TRANSPOSITION_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.TRANSPOSITION_ENGLISH)
                }

                5 -> Util.openCustomChromeTab(context, Const.REPLACEMENT_EPIC)

                6 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.REGISTRATION_OVERSEAS_VOTER_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.REGISTRATION_OVERSEAS_VOTER_ENGLISH)
                }

                7 -> Util.openCustomChromeTab(context, Const.TRACK_APPLICATION_STATUS)

                8 -> Util.openCustomChromeTab(context, Const.JK_MIGRANT_ELECTORS_12C)

                9 -> Util.openCustomChromeTab(context, Const.JK_MIGRANT_ELECTORS_12C)

                10 -> Util.openCustomChromeTab(context, Const.VOTERS_FAQ)

            }
        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textView = view.textView

    }

}