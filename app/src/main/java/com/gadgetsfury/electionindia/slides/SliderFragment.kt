package com.gadgetsfury.electionindia.slides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

import com.gadgetsfury.electionindia.R
import kotlinx.android.synthetic.main.fragment_slider.*

class SliderFragment : Fragment() {

    private var imageId: Int? = null
    private var title: String? = null
    private var description: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(imageView).load(imageId).into(imageView)
        textViewTitle.text = title
        textViewDesc.text = description

    }

    fun setData(imageId: Int, title: String, description: String) {
        this.imageId = imageId
        this.title = title
        this.description = description
    }

}
