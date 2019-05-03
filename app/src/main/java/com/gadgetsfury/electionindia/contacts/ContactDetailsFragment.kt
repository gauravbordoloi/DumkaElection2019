package com.gadgetsfury.electionindia.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_contact_details.*

class ContactDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contact = arguments!!.getParcelable<Contact>("contact")!!

        textViewName.text = contact.name
        textViewDesignation.text = contact.designation

        if (!contact.ofcNo.isNullOrEmpty()) {
            textViewOfficePhone.text = getString(R.string.contacts_phone_o, contact.ofcNo)
        } else {
            textViewOfficePhone.visibility = View.GONE
        }
        if (!contact.fax.isNullOrEmpty()) {
            textViewOfficeFax.text = getString(R.string.contacts_fax_o, contact.fax)
        } else {
            textViewOfficeFax.visibility = View.GONE
        }
        if (!contact.phone.isNullOrEmpty()) {
            textViewPhone.text = getString(R.string.contacts_phone_p, contact.phone)
        } else {
            textViewPhone.visibility = View.GONE
        }
        if (!contact.email.isNullOrEmpty()) {
            textViewEmail.text = getString(R.string.contacts_email, contact.email)
        } else {
            textViewEmail.visibility = View.GONE
        }

    }

}
