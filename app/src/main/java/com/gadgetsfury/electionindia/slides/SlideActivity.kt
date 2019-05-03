package com.gadgetsfury.electionindia.slides

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.home.HomeActivity
import com.gadgetsfury.electionindia.util.SharedPref
import kotlinx.android.synthetic.main.activity_slide.*

class SlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)

        SharedPref(this).dismissFirstTime()

        val pagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = pagerAdapter
        sliderDot.setViewPager(viewPager)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                if (position == 2) {
                    btnNext.visibility = View.GONE
                    btnSkip.visibility = View.GONE
                    btnStart.visibility = View.VISIBLE
                } else {
                    btnNext.visibility = View.VISIBLE
                    btnSkip.visibility = View.VISIBLE
                    btnStart.visibility = View.GONE
                }

            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < 2) {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }

        btnSkip.setOnClickListener {
            skip()
        }

        btnStart.setOnClickListener {
            skip()
        }

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private fun skip() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}
