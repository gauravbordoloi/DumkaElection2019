package com.gadgetsfury.electionindia.util.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.layout_image_dialog.*

class ImageViewDialog(context: Context, val image: String): Dialog(context, R.style.AppTheme_FullScreenDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        window!!.setLayout(width, height)
        setContentView(R.layout.layout_image_dialog)

        btnClose.setOnClickListener {
            dismiss()
        }

        Glide.with(imageView).load(image).apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)).into(imageView)

    }

}