package com.gadgetsfury.electionindia.settings

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.util.Util
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_feedback.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(activity!!)

        btnSubmit.setOnClickListener {

            if (validate()) {

                progressDialog.show()

                val name = nameBox.text.toString()
                val email = emailBox.text.toString()
                val phone = phoneBox.text.toString()
                val message = messageBox.text.toString()

                val map = HashMap<String, String>()
                map["message"] = message
                map["name"] = name
                map["email"] = email
                map["phone"] = phone

                ApiClient.getClient()
                    .create(ApiInterface::class.java)
                    .postFeedback(map)
                    .enqueue(object: Callback<ServerResponse?> {
                        override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                            progressDialog.show()
                            Log.e(TAG, t.message)
                            Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                            progressDialog.dismiss()
                            if (response.body() != null) {
                                if (response.body()!!.code == "success") {
                                    Toast.makeText(activity!!, getString(R.string.feedback_submitted_successfully), Toast.LENGTH_SHORT).show()
                                    messageBox.text = null
                                    nameBox.text = null
                                    emailBox.text = null
                                    phoneBox.text = null
                                } else {
                                    Toast.makeText(activity!!, response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "Null Response")
                            }
                        }
                    })

            }

        }

        btnEmail.setOnClickListener {
            Util.sendMailToSupport(activity!!)
        }

    }

    private fun validate(): Boolean {

        var valid = true

        val name = nameBox.text.toString()
        val email = emailBox.text.toString()
        val phone = phoneBox.text.toString()
        val message = messageBox.text.toString()

        if (name.isEmpty()) {
            valid = false
            nameBox.error = getString(R.string.this_field_is_required)
        } else {
            nameBox.error = null
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false
            emailBox.error = getString(R.string.this_field_is_required)
        } else {
            emailBox.error = null
        }

        if (message.isEmpty()) {
            valid = false
            messageBox.error = getString(R.string.this_field_is_required)
        } else {
            messageBox.error = null
        }

        if (phone.isNotEmpty() && phone.length != 10) {
            valid = false
            phoneBox.error = getString(R.string.ten_digit_required)
        } else {
            phoneBox.error = null
        }

        return valid
    }

}
