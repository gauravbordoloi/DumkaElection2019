package com.gadgetsfury.electionindia.slides

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gadgetsfury.electionindia.R

class ViewPagerAdapter(context: Context, fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    companion object {
        const val NUM_PAGES = 3
    }

    val titles = context.resources.getStringArray(R.array.slider_titles)
    val descriptions = context.resources.getStringArray(R.array.slider_descriptions)
    val images = context.resources.obtainTypedArray(R.array.slider_icons)

    override fun getItem(position: Int): Fragment {
        val f = SliderFragment()
        f.setData(images.getResourceId(position, 0),titles[position],descriptions[position])
        return f
    }

    override fun getCount(): Int = NUM_PAGES

}