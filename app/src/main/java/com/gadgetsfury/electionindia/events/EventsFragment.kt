package com.gadgetsfury.electionindia.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.gadgetsfury.electionindia.R
import com.gadgetsfury.electionindia.db.DatabaseCreator
import com.gadgetsfury.electionindia.util.LinearItemDecoration
import com.gadgetsfury.electionindia.util.dialog.ProgressDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_events.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.threeten.bp.LocalDate
import java.util.*

class EventsFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    //private lateinit var progressDialog: ProgressDialog

    private var eventsList: List<ElectionEvent>? = null

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_events, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //progressDialog = ProgressDialog(activity!!)

        spinner.adapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_1, arrayOf(getString(R.string.calendar_view), getString(R.string.list_view)))

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearItemDecoration(18,18, 36, 36))
        val adapter = EventAdapter(activity!!)
        recyclerView.adapter = adapter


        val eventDao = DatabaseCreator().getAppDatabase(activity!!.applicationContext).eventDao()
        doAsync {
            val list = eventDao.getAll()
            uiThread {
                if (list.isNullOrEmpty()) {
                    //progressDialog.show()
                } else {
                    eventsList = list
                    adapter.setList(list)

                    val events = HashSet<CalendarDay>()
                    for (e in list) {
                        val d = Date(e.startTimestamp)
                        val cal = Calendar.getInstance()
                        cal.time = d
                        events.add(CalendarDay.from(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))))
                    }
                    calendarView.addDecorator(EventDecorator(activity!!, events))
                }
            }
        }

        val model = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        model.getEvents().observe(this, Observer<List<ElectionEvent>>{
            /*if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }*/
            if (!it.isNullOrEmpty()) {

                eventsList = it
                adapter.setList(it)

                val events = HashSet<CalendarDay>()
                for (e in it) {
                    val d = Date(e.startTimestamp)
                    val cal = Calendar.getInstance()
                    cal.time = d
                    events.add(CalendarDay.from(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))))
                }
                calendarView.addDecorator(EventDecorator(activity!!, events))

                doAsync {
                    eventDao.clearAll()
                    eventDao.insertAll(it)
                }

            }
        })

        calendarView.setOnDateChangedListener { _, date, _ ->

            if (!eventsList.isNullOrEmpty()) {

                var ee: ElectionEvent? = null
                for (e in eventsList!!) {
                    val d = Date(e.startTimestamp)
                    val cal = Calendar.getInstance()
                    cal.time = d
//                    Log.e(TAG, "Year -> ${cal.get(Calendar.YEAR)}, Month -> ${cal.get(Calendar.MONTH) + 1}, Day -> ${cal.get(Calendar.DAY_OF_MONTH)}")
//                    Log.e(TAG, "Year -> ${date.year}, Month -> ${date.month}, Day -> ${date.day}")
                    if (cal.get(Calendar.YEAR) == date.year && (cal.get(Calendar.MONTH) + 1) == date.month &&
                        cal.get(Calendar.DAY_OF_MONTH) == date.day) {
                        ee = e
                        break
                    }
                }
                if (ee != null) {
                    findNavController().navigate(EventsFragmentDirections.actionEventsFragmentNavToEventDetailsFragment(ee))
                }
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                if (selectedItemView != null) {
                    when(position) {
                        0 -> {
                            recyclerView.visibility = View.GONE
                            calendarView.visibility = View.VISIBLE
                        }

                        1 -> {
                            recyclerView.visibility = View.VISIBLE
                            calendarView.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }

        }

    }

}
