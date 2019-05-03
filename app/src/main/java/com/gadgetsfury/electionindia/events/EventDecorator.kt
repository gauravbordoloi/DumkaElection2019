package com.gadgetsfury.electionindia.events

import android.content.Context
import androidx.core.content.ContextCompat
import com.gadgetsfury.electionindia.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorator(val context: Context, val dates: HashSet<CalendarDay>): DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.event_ring)!!)
    }

}