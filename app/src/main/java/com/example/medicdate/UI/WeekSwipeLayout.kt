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
    private val todayCalendar: Calendar = Calendar.getInstance()

    private var onDateChangeListener: ((String) -> Unit)? = null
    var onWeekSwipe: ((Int) -> Unit)? = null

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

        mondayCalendar.time = todayCalendar.time
        mondayCalendar.firstDayOfWeek = Calendar.MONDAY
        mondayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        findAndSetFirstValidDateInWeek()
    }

    private fun findAndSetFirstValidDateInWeek() {
        val temp = mondayCalendar.clone() as Calendar
        var found = false
        for (i in 0..6) {
            val dayOfWeek = temp.get(Calendar.DAY_OF_WEEK)
            val isWeekday = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
            val isFuture = temp.after(todayCalendar)

            if (isFuture && isWeekday) {
                selectedCalendar.time = temp.time
                found = true
                break
            }
            temp.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (!found) {
            val nextWeek = mondayCalendar.clone() as Calendar
            nextWeek.add(Calendar.WEEK_OF_YEAR, 1)
            selectedCalendar.time = nextWeek.time
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

        val dayOfWeek = temp.get(Calendar.DAY_OF_WEEK)
        val isWeekday = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
        val isFuture = temp.after(todayCalendar)

        if (!isFuture || !isWeekday) {
            return
        }

        selectedCalendar.time = temp.time
        updateWeekUi()
        onDateChangeListener?.invoke(currentDateString)
    }

    fun changeWeek(direction: Int) {
        val currentWeekMonday = todayCalendar.clone() as Calendar
        currentWeekMonday.firstDayOfWeek = Calendar.MONDAY
        currentWeekMonday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        if (direction < 0 && !mondayCalendar.after(currentWeekMonday)) {
            return
        }

        mondayCalendar.add(Calendar.WEEK_OF_YEAR, direction)
        findAndSetFirstValidDateInWeek()
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

            val dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK)
            val isWeekday = dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
            val isFuture = tempCal.after(todayCalendar)
            val isValidDate = isFuture && isWeekday

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
                } else if (isValidDate) {
                    child.setBackgroundResource(R.drawable.bg_date_default)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    child.alpha = 1.0f
                    child.isEnabled = true
                    child.isClickable = true
                } else {
                    child.setBackgroundResource(R.drawable.bg_date_default)
                    tvDayName?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    tvDayNum?.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                    child.alpha = 0.35f
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
