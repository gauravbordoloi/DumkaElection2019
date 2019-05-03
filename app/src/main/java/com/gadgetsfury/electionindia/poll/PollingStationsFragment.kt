package com.gadgetsfury.electionindia.poll

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.networking.ApiClient
import com.gadgetsfury.electionindia.networking.ApiInterface
import com.gadgetsfury.electionindia.util.GeoUtil
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import com.gadgetsfury.electionindia.util.SingleShotLocationProvider
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_polling_stations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.Dexter
import android.widget.Toast
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.db.dao.PollStationDao
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class PollingStationsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private var showType = ShowType.NEARBY_ME

    private lateinit var progressDialog: ProgressDialog

    private var latLng: LatLng? = null

    private lateinit var pollStationDao: PollStationDao

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_polling_stations, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val constituency = arguments!!.getString("constituency")!!
        val block = arguments!!.getString("block")!!

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18, 18, 36, 36))
        val adapter = PollingStationAdapter(activity!!)
        recyclerView.adapter = adapter

        pollStationDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).pollStationDao()
        progressDialog = ProgressDialog(activity!!)
        doAsync {
            val list = pollStationDao.getByBlock(constituency, block)
            uiThread {
                if (list.isNullOrEmpty()) {
                    Log.e(TAG, "1st")
                    //progressDialog.show()
                } else {
                    Log.e(TAG, "4th")
                    adapter.setList(list)
                }
            }
        }

        ApiClient.getClient().create(ApiInterface::class.java)
            .getPolls(constituency, block)
            .enqueue(object: Callback<List<PollingStation>?> {
                override fun onFailure(call: Call<List<PollingStation>?>, t: Throwable) {
                    Log.e(TAG, t.message)
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<PollingStation>?>, response: Response<List<PollingStation>?>) {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                    if (response.body() != null) {
                        adapter.setList(response.body()!!)
                        Log.e(TAG, "2nd")

                        doAsync {
                            Log.e(TAG, "3rd")
                            pollStationDao.clearAllByBlock(constituency, block)
                            pollStationDao.insertAll(response.body()!!)
                        }
                    } else {
                        Log.e(TAG, "Body is Null")
                        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    }
                }
            })

        /*val model = ViewModelProviders.of(this).get(PollsViewModel::class.java)
        model.getPollingStations().observe(this, Observer<List<PollingStation>> {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
            if (!it.isNullOrEmpty()) {
                adapter.setList(it)
                doAsync {
                    pollStationDao.clearAll()
                    pollStationDao.insertAll(it)
                }
            }
        })*/

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.toString().isNotEmpty()) {
                    showType = ShowType.CLEAR
                    btnAction.text = getString(R.string.clear)
                    adapter.filter.filter(s)
                } else {
                    showType = ShowType.NEARBY_ME
                    btnAction.text = getString(R.string.nearby_me)
                }

            }
        })

        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()) {

                        if (GeoUtil.isLocationEnabled(activity!!)) {

                            SingleShotLocationProvider.requestSingleUpdate(
                                activity!!,
                                object : SingleShotLocationProvider.LocationCallback {
                                    override fun onNewLocationAvailable(location: SingleShotLocationProvider.GPSCoordinates) {

                                        if (location.latitude != 0f && location.longitude != 0f) {

                                            latLng = LatLng(location.latitude.toDouble(), location.longitude.toDouble())
                                            Log.e(TAG, latLng.toString())

                                        } else {
                                            Log.e(TAG, "Null location")
                                        }

                                    }
                                })

                        } else {

                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)

                        }

                    } else {
                        Toast.makeText(activity!!, getString(R.string.permission_required_to_show_nearby_polls), Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()

        val api = ApiClient.getClient().create(ApiInterface::class.java)

        btnAction.setOnClickListener {

            when (showType) {

                ShowType.NEARBY_ME -> {
                    if (latLng != null) {

                        progressDialog.show()
                        api.getPollsNearbyMe(latLng!!.latitude, latLng!!.longitude, GeoUtil.NEARBY_RADIUS)
                            .enqueue(object : Callback<List<PollingStation>?> {
                                override fun onFailure(call: Call<List<PollingStation>?>, t: Throwable) {
                                    progressDialog.dismiss()
                                    Log.e(TAG, t.message)
                                }

                                override fun onResponse(
                                    call: Call<List<PollingStation>?>,
                                    response: Response<List<PollingStation>?>
                                ) {
                                    progressDialog.dismiss()
                                    if (response.body() != null) {
                                        showType = ShowType.SHOW_ALL
                                        btnAction.text = getString(R.string.show_all)
                                        adapter.setList(response.body()!!)
                                    } else {
                                        Log.e(TAG, "Null Data")
                                    }
                                }
                            })

                    } else {
                        Toast.makeText(activity!!, getString(R.string.fetching_your_location), Toast.LENGTH_SHORT).show()
                    }
                }

                ShowType.SHOW_ALL -> {
                    searchBox.setText("")
                    progressDialog.show()
                    /*model.getPollingStations().observe(this, Observer<List<PollingStation>> {
                        progressDialog.dismiss()
                        if (!it.isNullOrEmpty()) {
                            adapter.setList(it)
                        } else {
                            Log.e(TAG, "null")
                            showType = ShowType.SHOW_ALL
                            btnAction.text = "Show All"
                        }
                    })*/

                    ApiClient.getClient().create(ApiInterface::class.java)
                        .getPolls(constituency, block)
                        .enqueue(object: Callback<List<PollingStation>?> {
                            override fun onFailure(call: Call<List<PollingStation>?>, t: Throwable) {
                                Log.e(TAG, t.message)
                                if (progressDialog.isShowing) {
                                    progressDialog.dismiss()
                                }
                                Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                                showType = ShowType.SHOW_ALL
                                btnAction.text = getString(R.string.show_all)
                            }

                            override fun onResponse(call: Call<List<PollingStation>?>, response: Response<List<PollingStation>?>) {
                                if (progressDialog.isShowing) {
                                    progressDialog.dismiss()
                                }
                                if (response.body() != null) {
                                    adapter.setList(response.body()!!)
                                } else {
                                    Log.e(TAG, "Body is Null")
                                    Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                                    showType = ShowType.SHOW_ALL
                                    btnAction.text = getString(R.string.show_all)
                                }
                            }
                        })

                }

                ShowType.CLEAR -> {
                    searchBox.setText("")
                    progressDialog.show()

                    doAsync {
                        val list = pollStationDao.getByBlock(constituency, block)
                        uiThread {
                            if (list.isNullOrEmpty()) {
                                /*model.getPollingStations().observe(this@PollingStationsFragment, Observer<List<PollingStation>> {
                                    progressDialog.dismiss()
                                    if (!it.isNullOrEmpty()) {
                                        adapter.setList(it)
                                    } else {
                                        Log.e(TAG, "null")
                                        showType = ShowType.CLEAR
                                        btnAction.text = "Clear"
                                    }
                                })*/
                                ApiClient.getClient().create(ApiInterface::class.java)
                                    .getPolls(constituency, block)
                                    .enqueue(object: Callback<List<PollingStation>?> {
                                        override fun onFailure(call: Call<List<PollingStation>?>, t: Throwable) {
                                            Log.e(TAG, t.message)
                                            if (progressDialog.isShowing) {
                                                progressDialog.dismiss()
                                            }
                                            Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                                            showType = ShowType.CLEAR
                                            btnAction.text = getString(R.string.clear)
                                        }

                                        override fun onResponse(call: Call<List<PollingStation>?>, response: Response<List<PollingStation>?>) {
                                            if (progressDialog.isShowing) {
                                                progressDialog.dismiss()
                                            }
                                            if (response.body() != null) {
                                                adapter.setList(response.body()!!)
                                            } else {
                                                Log.e(TAG, "Body is Null")
                                                Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                                                showType = ShowType.CLEAR
                                                btnAction.text = getString(R.string.clear)
                                            }
                                        }
                                    })
                            } else {
                                if (progressDialog.isShowing) {
                                    progressDialog.dismiss()
                                }
                                adapter.setList(list)
                            }
                        }
                    }
                }
            }

        }

    }

    override fun onDestroy() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroy()
    }

}
