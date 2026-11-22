package com.example.medicdate.UI

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
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
) : LinearLayout(context, attrs, defStyleAttr) {

    private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayNumFormat = SimpleDateFormat("d", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMM", Locale("es", "MX"))

    private val mondayCalendar: Calendar = Calendar.getInstance()
    private val selectedCalendar: Calendar = Calendar.getInstance()

    private var onDateChangeListener: ((String) -> Unit)? = null
    var onWeekSwipe: ((Int) -> Unit)? = null

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var startX = 0f
    private var startY = 0f

    val currentDateString: String
        get() = apiDateFormat.format(selectedCalendar.time)

    init {
        orientation = HORIZONTAL
        mondayCalendar.firstDayOfWeek = Calendar.MONDAY
        mondayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val today = Calendar.getInstance()
        selectedCalendar.time = mondayCalendar.time
        if (!selectedCalendar.after(today)) {
            selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)
            if (selectedCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                selectedCalendar.add(Calendar.DAY_OF_YEAR, 2)
            } else if (selectedCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                selectedCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setupDayViews()
        updateWeekUi()
    }

    private fun setupDayViews() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val dayIndex = i
            child.setOnClickListener {
                selectDayInCurrentWeek(dayIndex)
            }
        }
    }

    private fun selectDayInCurrentWeek(dayIndex: Int) {
        val temp = mondayCalendar.clone() as Calendar
        temp.add(Calendar.DAY_OF_YEAR, dayIndex)
        selectedCalendar.time = temp.time
        updateWeekUi()
        onDateChangeListener?.invoke(currentDateString)
    }

    fun changeWeek(direction: Int) {
        mondayCalendar.add(Calendar.WEEK_OF_YEAR, direction)
        val dayOfWeekOffset = (selectedCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7
        val temp = mondayCalendar.clone() as Calendar
        temp.add(Calendar.DAY_OF_YEAR, dayOfWeekOffset)
        selectedCalendar.time = temp.time
        updateWeekUi()
        onDateChangeListener?.invoke(currentDateString)
    }

    fun setOnDateChangeListener(listener: (String) -> Unit) {
        onDateChangeListener = listener
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
            val isWeekend = i >= 5

            if (child is LinearLayout && child.childCount >= 2) {
                val tvDayName = child.getChildAt(0) as? TextView
                val tvDayNum = child.getChildAt(1) as? TextView

                tvDayName?.text = dayNames[i]
                tvDayNum?.text = dayNumFormat.format(tempCal.time)

                if (isSelected) {
                    child.setBackgroundResource(R.drawable.bg_date_selected)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.on_brand))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.on_brand))
                    child.alpha = 1.0f
                } else {
                    child.setBackgroundResource(R.drawable.bg_date_default)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    child.alpha = if (isWeekend) 0.45f else 1.0f
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
                    onWeekSwipe?.invoke(direction)
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
}
