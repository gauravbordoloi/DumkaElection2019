package com.gadgetsfury.electionindia.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.content.res.AppCompatResources
import com.gadgetsfury.electionindia.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class GeoUtil {

    companion object {

        private const val MARKER_CAP = "http://bit.ly/2I5Zpve"

        val DUMKA_BOUNDS = LatLngBounds(LatLng(23.984617, 86.903659), LatLng(24.640530, 87.688464))

        const val AVG_ZOOM_LEVEL = 17.toFloat()
        const val NEARBY_RADIUS = 5000

        fun isLocationEnabled(context: Context): Boolean {
            val locationMode: Int
            val locationProviders: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                    return false
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF
            } else {
                locationProviders =
                    Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }

        fun getMapImageLink(context: Context, latLng: LatLng, zoom: Int, width: Int, height: Int): String {

            return Uri.encode("https://maps.googleapis.com/maps/api/staticmap?" +
                    "markers=icon:" + MARKER_CAP + "|anchor:center|" + latLng.latitude + "," + latLng.longitude +
                    "&zoom=" + zoom + "&size=" + width + "x" + height+ "&key=" + context.resources.getString(R.string.google_maps_api), "@#&=*+-_.,:!?()/~'%")

        }

        fun customizeMap(map: GoogleMap): GoogleMap {
            map.setLatLngBoundsForCameraTarget(DUMKA_BOUNDS)
            map.setMaxZoomPreference(20f)
            /*val style = MapStyleOptions.loadRawResourceStyle(
                context, R.raw.map)
            map.setMapStyle(style)*/
            map.uiSettings.isMyLocationButtonEnabled = false
            map.uiSettings.isCompassEnabled = false
            map.uiSettings.isMapToolbarEnabled = false
            map.uiSettings.isZoomControlsEnabled = false
            map.uiSettings.isRotateGesturesEnabled = false
            map.uiSettings.isTiltGesturesEnabled = false
            return map
        }

        fun getBitmapFromDrawable(context: Context, resourceId: Int, size: Int): Bitmap {
            val bitmapDrawable = AppCompatResources.getDrawable(context, resourceId) as BitmapDrawable
            val b = bitmapDrawable.bitmap
            return Bitmap.createScaledBitmap(b, size, size, false)
        }

    }

}