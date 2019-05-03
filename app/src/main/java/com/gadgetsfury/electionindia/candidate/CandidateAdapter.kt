package com.gadgetsfury.electionindia.candidate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_candidate_item.view.*

class CandidateAdapter(val context: Context): RecyclerView.Adapter<CandidateAdapter.ViewHolder>(), Filterable {

    private var list: List<Candidate>? = null
    private var filteredList: List<Candidate>? = null

    private val glide = Glide.with(context)
    private val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)

    fun setList(list: List<Candidate>) {
        this.list = list
        this.filteredList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_candidate_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (filteredList.isNullOrEmpty()) {
            0
        } else {
            return filteredList!!.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val candidate = filteredList!![position]

        holder.textViewName.text = candidate.name
        holder.textViewDistrict.text = candidate.district
        glide.load(candidate.image).apply(requestOptions).into(holder.imageView)
        glide.load(candidate.symbol).apply(requestOptions).into(holder.imageViewButton)

    }

    override fun getFilter(): Filter {

        return object: Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                Log.e("TAG", charString)
                if (charString.isEmpty() || charString == "All") {
                    filteredList = list
                } else {
                    val tempList = ArrayList<Candidate>()
                    for (row in list!!) {

                        if (row.district == charString) {
                            tempList.add(row)
                        }
                    }

                    filteredList = tempList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    filteredList = results.values as ArrayList<Candidate>
                    notifyDataSetChanged()
                }
            }

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView = view.imageView
        val imageViewButton = view.imageViewButton
        val textViewName = view.textViewName
        val textViewDistrict = view.textViewDistrict

    }

}