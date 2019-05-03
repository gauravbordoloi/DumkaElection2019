package com.gadgetsfury.electionindia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gadgetsfury.electionindia.util.GeoUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnPos.setOnClickListener {
            if (mMap != null) {
                mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(GeoUtil.DUMKA_BOUNDS, 20))
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = GeoUtil.customizeMap(googleMap!!)
        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(GeoUtil.DUMKA_BOUNDS, 20))
    }

}
