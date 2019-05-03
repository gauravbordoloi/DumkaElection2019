package com.gadgetsfury.electionindia.sveep.vfeed

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.util.Util
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_vfeed_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class VFeedDetailsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private var image: Bitmap? = null
    private var feed: VoterFeed? = null

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vfeed_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments!!.getInt("id")
        val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())

        progressDialog = ProgressDialog(activity!!)
        progressDialog.show()

        ApiClient.getClient().create(ApiInterface::class.java)
            .getVotersFeedById(id)
            .enqueue(object: Callback<VoterFeed?> {
                override fun onFailure(call: Call<VoterFeed?>, t: Throwable) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Log.e(TAG, t.message)
                    Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<VoterFeed?>, response: Response<VoterFeed?>) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    if (response.body() != null) {

                        feed = response.body()!!

                        textViewName.text = feed!!.name
                        Glide.with(activity!!).asBitmap().load(feed!!.image).apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
                            .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                image = resource
                                imageView.setImageBitmap(image)
                            }

                        })
                        textViewTime.text = sdf.format(Date(feed!!.timestamp))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            textViewContent.text = Html.fromHtml(feed!!.content, Html.FROM_HTML_MODE_COMPACT)
                        } else {
                            textViewContent.text = Html.fromHtml(feed!!.content)
                        }

                    } else {
                        Log.e(TAG, "Null Response")
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                }
            })

        btnShare.setOnClickListener {
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        if (image != null) {
                            Util.shareImageAndText(activity!!, image!!, System.currentTimeMillis().toString(),
                                "*${feed!!.name}* \n\n${feed!!.content}")
                        } else {
                            Util.shareText(activity!!, "*${feed!!.name}* \n\n${feed!!.content}")
                        }
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(activity!!, getString(R.string.permission_required_to_share_feeds), Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

    }

    override fun onDestroy() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroy()
    }

}
