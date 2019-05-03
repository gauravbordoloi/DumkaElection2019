package com.gadgetsfury.electionindia.sveep.vod

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import kotlinx.android.synthetic.main.fragment_vod_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class VodDetailsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vod_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments!!.getInt("id")
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        progressDialog = ProgressDialog(activity!!)
        progressDialog.show()

        ApiClient.getClient()
            .create(ApiInterface::class.java)
            .getVodById(id)
            .enqueue(object: Callback<VOD?> {
                override fun onFailure(call: Call<VOD?>, t: Throwable) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Log.e(TAG, t.message)
                    Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<VOD?>, response: Response<VOD?>) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    if (response.body() != null) {

                        val vod = response.body()!!

                        textViewDate.text = "${getString(R.string.vod)} - ${sdf.format(Date(vod.timestamp))}"
                        Glide.with(imageView).load(vod.image).apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)).into(imageView)
                        textViewName.text = vod.name
                        textViewContent.text = vod.content

                    } else {
                        Log.e(TAG, "Response Null")
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                }
            })


    }

}
