package com.gadgetsfury.electionindia.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Const
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.layout_game_item.view.*
import java.util.*

class GameAdapter(val context: Context): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private val titles = context.resources.getStringArray(R.array.game_title)
    private val descriptions = context.resources.getStringArray(R.array.game_descriptions)
    private val drawables = context.resources.obtainTypedArray(R.array.game_images)

    private val glide = Glide.with(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_game_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textViewTitle.text = titles[position]
        holder.textViewDesc.text = descriptions[position]
        glide.load(drawables.getResourceId(position, 0)).into(holder.imageView)

        holder.itemView.setOnClickListener {

            var isHindi = false
            if (Locale.getDefault().displayLanguage == "हिन्दी") {
                isHindi = true
            }

            when(position) {

                0 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_LUDO_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_LUDO_ENGLISH)
                }

                1 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_MAZE_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_MAZE_ENGLISH)
                }

                2 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_SNAKE_AND_LADDER_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_SNAKE_AND_LADDER_ENGLISH)
                }

                3 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_VIGILANT_VOTER_CARD_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_VIGILANT_VOTER_CARD_ENGLISH)
                }

                4 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_REPRESENTATIVE_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_REPRESENTATIVE_ENGLISH)
                }

                5 -> if (isHindi) {
                    Util.openCustomChromeTab(context, Const.GAME_INFORMED_VOTER_HINDI)
                } else {
                    Util.openCustomChromeTab(context, Const.GAME_INFORMED_VOTER_ENGLISH)
                }

                6 -> Util.openCustomChromeTab(context, Const.GAME_VARNMALA)

            }

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewTitle = view.textViewTitle
        val imageView = view.imageView
        val textViewDesc = view.textViewDesc

    }

}