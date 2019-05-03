package com.gadgetsfury.electionindia.home

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        nav_view.setNavigationItemSelectedListener(this)

        btnMenu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (!report!!.areAllPermissionsGranted()) {

                        Toast.makeText(this@HomeActivity, getString(R.string.permissions_required), Toast.LENGTH_SHORT).show()

                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()

    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {

        when(menu.itemId) {
            R.id.navHome -> if (findNavController(R.id.nav_host_fragment).currentDestination!!.id != R.id.homeFragment) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
            }

            R.id.navEvents -> findNavController(R.id.nav_host_fragment).navigate(R.id.eventsFragmentNav)

            R.id.navVoterServices -> findNavController(R.id.nav_host_fragment).navigate(R.id.voterServicesFragment)

            R.id.navVoterEducation -> Util.openCustomChromeTab(this, Const.VOTERS_EDUCATION)

            R.id.navContacts -> findNavController(R.id.nav_host_fragment).navigate(R.id.contactsFragment)

            R.id.navKYC -> findNavController(R.id.nav_host_fragment).navigate(R.id.KYCFragment)

            R.id.navFeedback -> findNavController(R.id.nav_host_fragment).navigate(R.id.feedbackFragment)

            R.id.navAbout -> findNavController(R.id.nav_host_fragment).navigate(R.id.aboutFragment)

            R.id.navCandidateList -> findNavController(R.id.nav_host_fragment).navigate(R.id.candidatesFragment)

            R.id.navShare -> Util.shareApp(this)

            R.id.navSettings -> findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            findNavController(R.id.nav_host_fragment).currentDestination!!.id == R.id.homeFragment -> this.finish()
            else -> super.onBackPressed()
        }
    }

}
