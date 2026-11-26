package com.example.medicdate.UI

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.medicdate.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

class WeekSwipeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    interface OnDateChangeListener {
        fun onDateChanged(newDate: String)
    }

    interface OnWeekSwipeListener {
        fun onWeekSwiped(direction: Int)
    }

    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNumFormat = SimpleDateFormat("d", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMM", Locale("es", "MX"))

    private val mondayCalendar: Calendar = Calendar.getInstance()
    private val selectedCalendar: Calendar = Calendar.getInstance()
    private val todayCalendar: Calendar = Calendar.getInstance()

    private var doctorWorkingDays: Set<Int> = emptySet()
    private var workingDaysLoaded: Boolean = false
    private var onDateChangeListener: OnDateChangeListener? = null
    private var onWeekSwipeListener: OnWeekSwipeListener? = null

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var startX = 0f
    private var startY = 0f

    val currentDateString: String
        get() = apiDateFormat.format(selectedCalendar.time)

    init {
        orientation = HORIZONTAL

        todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
        todayCalendar.set(Calendar.MINUTE, 0)
        todayCalendar.set(Calendar.SECOND, 0)
        todayCalendar.set(Calendar.MILLISECOND, 0)

        selectedCalendar.time = todayCalendar.time
        selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)

        mondayCalendar.time = selectedCalendar.time
        moverALunesDeSemana(mondayCalendar)
    }

    private fun moverALunesDeSemana(cal: Calendar) {
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }
    }

    fun setDoctorWorkingDays(days: Set<Int>): Boolean {
        val fechaAnterior = currentDateString
        val cambiaronDias = !workingDaysLoaded || doctorWorkingDays != days
        doctorWorkingDays = days
        workingDaysLoaded = true

        val fechaActualValida = selectedCalendar.after(todayCalendar) &&
            isDoctorWorkingDay(selectedCalendar)

        if (!fechaActualValida && doctorWorkingDays.isNotEmpty()) {
            val inicioBusqueda = todayCalendar.clone() as Calendar
            moverALunesDeSemana(inicioBusqueda)
            if (mondayCalendar.before(inicioBusqueda)) {
                mondayCalendar.time = inicioBusqueda.time
            }
            findAndSetFirstValidDateInWeek()
        }

        if (cambiaronDias || !fechaActualValida) {
            updateWeekUi()
        }

        return currentDateString != fechaAnterior
    }

    fun tieneDiasDeAtencion(): Boolean {
        return workingDaysLoaded && doctorWorkingDays.isNotEmpty()
    }

    private fun isDoctorWorkingDay(cal: Calendar): Boolean {
        if (!workingDaysLoaded || doctorWorkingDays.isEmpty()) {
            return false
        }
        val calDay = cal.get(Calendar.DAY_OF_WEEK)
        val isoDay = if (calDay == Calendar.SUNDAY) 7 else calDay - 1
        return doctorWorkingDays.contains(isoDay)
    }

    private fun findAndSetFirstValidDateInWeek(): Boolean {
        if (!workingDaysLoaded || doctorWorkingDays.isEmpty()) {
            return false
        }

        val searchMonday = mondayCalendar.clone() as Calendar
        moverALunesDeSemana(searchMonday)

        for (weekOffset in 0 until MAX_SEARCH_WEEKS) {
            val temp = searchMonday.clone() as Calendar
            for (dayOffset in 0..6) {
                val isWorking = isDoctorWorkingDay(temp)
                val isFuture = temp.after(todayCalendar)
                if (isFuture && isWorking) {
                    selectedCalendar.time = temp.time
                    mondayCalendar.time = temp.time
                    moverALunesDeSemana(mondayCalendar)
                    return true
                }
                temp.add(Calendar.DAY_OF_YEAR, 1)
            }
            searchMonday.add(Calendar.WEEK_OF_YEAR, 1)
        }
        return false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupDayViews()
        updateWeekUi()
    }

    private fun setupDayViews() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.tag = i
            child.setOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        val dayIndex = view.tag as? Int ?: return
        selectDayInCurrentWeek(dayIndex)
    }

    private fun selectDayInCurrentWeek(dayIndex: Int) {
        if (!workingDaysLoaded || doctorWorkingDays.isEmpty()) {
            return
        }

        val temp = mondayCalendar.clone() as Calendar
        temp.add(Calendar.DAY_OF_YEAR, dayIndex)

        val isWorking = isDoctorWorkingDay(temp)
        val isFuture = temp.after(todayCalendar)

        if (!isFuture || !isWorking) {
            return
        }

        selectedCalendar.time = temp.time
        updateWeekUi()
        onDateChangeListener?.onDateChanged(currentDateString)
    }

    fun changeWeek(direction: Int) {
        if (!workingDaysLoaded || doctorWorkingDays.isEmpty()) {
            return
        }

        val currentWeekMonday = todayCalendar.clone() as Calendar
        moverALunesDeSemana(currentWeekMonday)

        if (direction < 0 && !mondayCalendar.after(currentWeekMonday)) {
            return
        }

        mondayCalendar.add(Calendar.WEEK_OF_YEAR, direction)
        moverALunesDeSemana(mondayCalendar)
        if (mondayCalendar.before(currentWeekMonday)) {
            mondayCalendar.time = currentWeekMonday.time
        }

        findAndSetFirstValidDateInWeek()
        updateWeekUi()
        onDateChangeListener?.onDateChanged(currentDateString)
    }

    fun setOnDateChangeListener(listener: OnDateChangeListener?) {
        onDateChangeListener = listener
    }

    fun setOnWeekSwipeListener(listener: OnWeekSwipeListener?) {
        onWeekSwipeListener = listener
    }

    fun getWeekRangeLabel(): String {
        val endCal = mondayCalendar.clone() as Calendar
        endCal.add(Calendar.DAY_OF_YEAR, 6)

        val startDay = dayNumFormat.format(mondayCalendar.time)
        val endDay = dayNumFormat.format(endCal.time)
        val monthStr = monthFormat.format(endCal.time).replace(".", "")
        val yearStr = SimpleDateFormat("yyyy", Locale.getDefault()).format(endCal.time)

        return "$startDay–$endDay $monthStr $yearStr"
    }

    fun updateWeekUi() {
        val tempCal = mondayCalendar.clone() as Calendar
        val selectedDateStr = apiDateFormat.format(selectedCalendar.time)
        val dayNames = arrayOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val dayDateStr = apiDateFormat.format(tempCal.time)
            val isSelected = dayDateStr == selectedDateStr

            val isWorking = isDoctorWorkingDay(tempCal)
            val isFuture = tempCal.after(todayCalendar)
            val isValidDate = isFuture && isWorking

            if (child is LinearLayout && child.childCount >= 2) {
                val tvDayName = child.getChildAt(0) as? TextView
                val tvDayNum = child.getChildAt(1) as? TextView

                tvDayName?.text = dayNames[i]
                tvDayNum?.text = dayNumFormat.format(tempCal.time)

                if (isSelected && isValidDate) {
                    child.setBackgroundResource(R.drawable.bg_date_selected)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.on_brand))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.on_brand))
                    child.alpha = 1.0f
                    child.isEnabled = true
                    child.isClickable = true
                    child.setOnClickListener(this)
                } else if (isValidDate) {
                    child.setBackgroundResource(R.drawable.bg_date_default)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    child.alpha = 1.0f
                    child.isEnabled = true
                    child.isClickable = true
                    child.setOnClickListener(this)
                } else {
                    child.setBackgroundResource(R.drawable.bg_date_default)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    child.alpha = 0.35f
                    child.setOnClickListener(null)
                    child.isEnabled = false
                    child.isClickable = false
                }
            }
            tempCal.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val distanceX = event.x - startX
                val distanceY = event.y - startY
                if (abs(distanceX) > touchSlop && abs(distanceX) > abs(distanceY)) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_UP -> {
                val distanceX = event.x - startX
                if (abs(distanceX) > touchSlop) {
                    val direction = if (distanceX < 0) 1 else -1
                    changeWeek(direction)
                    onWeekSwipeListener?.onWeekSwiped(direction)
                    performClick()
                    return true
                }
            }

            MotionEvent.ACTION_CANCEL -> return true
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    companion object {
        private const val MAX_SEARCH_WEEKS = 52
    }
}
