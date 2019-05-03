package com.gadgetsfury.electionindia.feeds

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.Util
import kotlinx.android.synthetic.main.fragment_news_feed.*
import java.text.SimpleDateFormat
import java.util.*
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.Dexter
import android.widget.Toast
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.karumi.dexter.listener.PermissionRequest


class NewsFeedFragment : Fragment() {

    private var image: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feed = arguments!!.getParcelable<Feed>("feed")!!

        val requestOptions = RequestOptions().error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
        Glide.with(activity!!).asBitmap().load(feed.image).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                image = resource
                imageView.setImageBitmap(image)
            }

        })
        textViewTitle.text = feed.title
        val sdf = SimpleDateFormat("dd MMM yy hh:mm a", Locale.getDefault())
        textViewTime.text = sdf.format(Date(feed.timestamp))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textViewContent.text = Html.fromHtml(feed.content, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textViewContent.text = Html.fromHtml(feed.content)
        }

        btnShare.setOnClickListener {

            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        if (image != null) {
                            Util.shareImageAndText(activity!!, image!!, System.currentTimeMillis().toString(),
                                "*${feed.title}* \n\n${feed.description}")
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

}
