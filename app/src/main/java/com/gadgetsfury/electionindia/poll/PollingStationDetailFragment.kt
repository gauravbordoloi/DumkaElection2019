package com.gadgetsfury.electionindia.poll

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.util.GeoUtil
import com.gadgetsfury.electionindia.util.Util
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_polling_station_detail.*
import org.json.JSONObject

class PollingStationDetailFragment : Fragment(), OnMapReadyCallback {

    private val TAG = this.javaClass.simpleName

    private var mMap: GoogleMap? = null
    private var latlng: LatLng? = null
    private var pollingStation: PollingStation? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_polling_station_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pollingStation = arguments!!.getParcelable("pollingStation")
        if (pollingStation!!.location != null) {
            latlng = LatLng(
                JSONObject(pollingStation!!.location).getDouble("lat"),
                JSONObject(pollingStation!!.location).getDouble("lng")
            )
            initMap()
        } else {
            btnNavigate.isEnabled = false
        }
        textViewTitle.text = pollingStation!!.title
        textViewBoothNo.text = getString(R.string.booth_no, pollingStation!!.boothNumber)
        textViewTotal.text = getString(R.string.total_voters, pollingStation!!.noOfVoters)
        textViewTotalPwd.text = getString(R.string.total_pwd_voters, pollingStation!!.noOfPwdVoters)

        if (pollingStation!!.bloName != null) {
            textViewBloName.text = getString(R.string.blo, pollingStation!!.bloName)
        }

        /*if (!pollingStation!!.images.isNullOrEmpty()) {
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 26, 10))
            val adapter = BoothImageAdapter(activity!!)
            recyclerView.adapter = adapter
            adapter.setList(pollingStation!!.images!!)
        } else {
            recyclerView.visibility = View.GONE
        }*/

        btnCall.setOnClickListener {
            if (pollingStation!!.phone == null) {
                return@setOnClickListener
            }
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Util.call(activity!!, pollingStation!!.phone!!)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Toast.makeText(context, getString(R.string.permission_required_to_call), Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

        btnNavigate.setOnClickListener {
            if (latlng != null) {
                Util.navigate(activity!!, latlng!!)
            }
        }

        btnMyPos.setOnClickListener {
            if (mMap != null && latlng != null) {

                mMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latlng, GeoUtil.AVG_ZOOM_LEVEL
                    )
                )

            }
        }

    }

    private fun initMap() {

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = GeoUtil.customizeMap(googleMap!!)
        if (latlng != null) {
            val myLocationBitmap = GeoUtil.getBitmapFromDrawable(activity!!, R.drawable.ic_marker, 100)
            mMap?.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(myLocationBitmap))
                    .position(latlng!!)
                    .flat(true)
                    .alpha(0.5f)
                    .anchor(0.5f, 0.5f)
                    .title(pollingStation!!.title)
            )
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng!!, GeoUtil.AVG_ZOOM_LEVEL))
        }
    }

}
