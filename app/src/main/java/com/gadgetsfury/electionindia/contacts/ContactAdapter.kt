package com.gadgetsfury.electionindia.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_contact_item.view.*

class ContactAdapter(val context: Context): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var list: List<Contact>? = null

    fun setList(list: List<Contact>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (list.isNullOrEmpty()) {
            0
        } else {
            return list!!.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contact = list!![position]

        holder.textViewName.text = contact.name
        holder.textViewDesignation.text = contact.designation

        holder.itemView.setOnClickListener {

            it.findNavController().navigate(ContactsFragmentDirections.actionContactsFragmentToContactDetailsFragment(contact))

        }

    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName = view.textViewName
        val textViewDesignation = view.textViewDesignation

    }

}