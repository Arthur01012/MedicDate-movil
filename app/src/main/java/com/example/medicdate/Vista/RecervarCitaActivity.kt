package com.example.medicdate.Vista

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicdate.R
import com.google.android.material.button.MaterialButton
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class RecervarCitaActivity : AppCompatActivity() {
    private val spanishLocale = Locale.forLanguageTag("es-MX")
    private val timeOptions = listOf(
        R.id.time_0900 to "09:00",
        R.id.time_0930 to "09:30",
        R.id.time_1030 to "10:30",
        R.id.time_1100 to "11:00",
        R.id.time_1230 to "12:30"
    )

    private lateinit var dateGroup: WeekSwipeLayout
    private lateinit var weekLabel: TextView
    private lateinit var firstBookableDate: LocalDate
    private lateinit var selectedDate: LocalDate
    private lateinit var visibleWeekStart: LocalDate
    private var selectedTime = "10:30"
    private var appointmentType = "Primera consulta"
    private var appointmentDuration = "30 min"
    private var weekIsAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recervar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dateGroup = findViewById(R.id.group_dates)
        weekLabel = findViewById(R.id.week_label)
        firstBookableDate = nextBookableDate(LocalDate.now().plusDays(1))
        selectedDate = firstBookableDate
        visibleWeekStart = firstBookableDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        dateGroup.onWeekSwipe = ::changeWeek
        findViewById<MaterialButton>(R.id.button_previous_week).setOnClickListener { changeWeek(-1) }
        findViewById<MaterialButton>(R.id.button_next_week).setOnClickListener { changeWeek(1) }
        findViewById<TextView>(R.id.button_back).setOnClickListener { finish() }

        findViewById<RadioGroup>(R.id.group_appointment_type).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_follow_up -> {
                    appointmentType = "Seguimiento"
                    appointmentDuration = "20 min"
                }
                else -> {
                    appointmentType = "Primera consulta"
                    appointmentDuration = "30 min"
                }
            }
            updateTypeSelection(checkedId)
            updateAppointmentSummary()
        }

        timeOptions.forEach { (viewId, time) ->
            findViewById<TextView>(viewId).setOnClickListener {
                selectedTime = time
                updateTimeSelection()
                updateAppointmentSummary()
            }
        }

        findViewById<MaterialButton>(R.id.button_continue).setOnClickListener {
            val booking = Intent(this, ConfirmarCitaActivity::class.java).apply {
                putExtra(CitaExtras.DATE_LABEL, shortDate(selectedDate))
                putExtra(CitaExtras.DATE_LONG, longDate(selectedDate))
                putExtra(CitaExtras.TIME, selectedTime)
                putExtra(CitaExtras.TYPE, appointmentType)
                putExtra(CitaExtras.DURATION, appointmentDuration)
                putExtra(CitaExtras.DOCTOR, getString(R.string.doctor_name))
                putExtra(CitaExtras.SPECIALTY, getString(R.string.doctor_specialty))
                putExtra(CitaExtras.LOCATION, getString(R.string.clinic_location))
            }
            startActivity(booking)
        }

        renderWeek()
        updateWeekLabel()
        updateTimeSelection()
        updateTypeSelection(R.id.radio_first_visit)
        updateAppointmentSummary()
    }

    private fun renderWeek() {
        for (index in 0 until dateGroup.childCount) {
            val date = visibleWeekStart.plusDays(index.toLong())
            val cell = dateGroup.getChildAt(index) as LinearLayout
            val weekday = cell.getChildAt(0) as TextView
            val dayNumber = cell.getChildAt(1) as TextView
            val available = !date.isBefore(firstBookableDate) &&
                date.dayOfWeek != DayOfWeek.SATURDAY &&
                date.dayOfWeek != DayOfWeek.SUNDAY
            val selected = date == selectedDate

            weekday.text = date.format(DateTimeFormatter.ofPattern("EEE", spanishLocale))
                .replace(".", "")
                .lowercase(spanishLocale)
            dayNumber.text = date.dayOfMonth.toString()
            cell.setBackgroundResource(
                if (selected) R.drawable.bg_date_selected else R.drawable.bg_date_default
            )
            cell.alpha = if (available) 1f else 0.45f
            cell.isEnabled = available
            cell.isClickable = available
            cell.isFocusable = available
            cell.isSelected = selected
            cell.contentDescription = longDate(date) +
                if (!available) ", ${getString(R.string.unavailable_date)}" else ""
            weekday.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (selected) R.color.on_brand else R.color.text_secondary
                )
            )
            dayNumber.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (selected) R.color.on_brand else R.color.text_primary
                )
            )
            cell.setOnClickListener {
                selectedDate = date
                renderWeek()
                updateAppointmentSummary()
            }
        }
    }

    private fun updateWeekLabel() {
        val weekEnd = visibleWeekStart.plusDays(6)
        val startMonth = monthShort(visibleWeekStart)
        val endMonth = monthShort(weekEnd)
        val label = when {
            visibleWeekStart.year != weekEnd.year ->
                "${visibleWeekStart.dayOfMonth} $startMonth ${visibleWeekStart.year}–" +
                    "${weekEnd.dayOfMonth} $endMonth ${weekEnd.year}"
            visibleWeekStart.month == weekEnd.month ->
                "${visibleWeekStart.dayOfMonth}–${weekEnd.dayOfMonth} $endMonth ${weekEnd.year}"
            else ->
                "${visibleWeekStart.dayOfMonth} $startMonth–${weekEnd.dayOfMonth} $endMonth ${weekEnd.year}"
        }
        weekLabel.text = label
        weekLabel.contentDescription =
            "Semana del ${longDate(visibleWeekStart)} al ${longDate(weekEnd)}"
        weekLabel.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
    }

    private fun changeWeek(direction: Int) {
        if (weekIsAnimating) return
        weekIsAnimating = true
        val distance = dateGroup.width.toFloat()
        if (distance == 0f) {
            finishWeekChange(direction, distance)
            return
        }

        val outgoingOffset = if (direction > 0) -distance else distance
        dateGroup.animate()
            .translationX(outgoingOffset)
            .setDuration(180L)
            .withEndAction {
                finishWeekChange(direction, -outgoingOffset)
            }
            .start()
    }

    private fun finishWeekChange(direction: Int, incomingOffset: Float) {
        visibleWeekStart = visibleWeekStart.plusWeeks(direction.toLong())
        renderWeek()
        updateWeekLabel()
        dateGroup.translationX = incomingOffset
        dateGroup.animate()
            .translationX(0f)
            .setDuration(180L)
            .withEndAction { weekIsAnimating = false }
            .start()
    }

    private fun updateTimeSelection() {
        timeOptions.forEach { (viewId, time) ->
            val option = findViewById<TextView>(viewId)
            val selected = time == selectedTime
            option.setBackgroundResource(
                if (selected) R.drawable.bg_time_selected else R.drawable.bg_time_default
            )
            option.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (selected) R.color.on_brand else R.color.text_primary
                )
            )
            option.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
            option.isSelected = selected
            option.contentDescription = if (selected) "$time, seleccionado" else time
        }
    }

    private fun updateTypeSelection(checkedId: Int) {
        listOf(R.id.radio_first_visit, R.id.radio_follow_up).forEach { viewId ->
            val option = findViewById<TextView>(viewId)
            val selected = viewId == checkedId
            option.setBackgroundResource(
                if (selected) R.drawable.bg_option_selected else R.drawable.bg_option_default
            )
        }
    }

    private fun updateAppointmentSummary() {
        findViewById<TextView>(R.id.selected_appointment_summary).text = getString(
            R.string.selected_date_format,
            shortDate(selectedDate),
            selectedTime,
            appointmentType
        )
    }

    private fun nextBookableDate(date: LocalDate): LocalDate {
        var candidate = date
        while (candidate.dayOfWeek == DayOfWeek.SATURDAY ||
            candidate.dayOfWeek == DayOfWeek.SUNDAY
        ) {
            candidate = candidate.plusDays(1)
        }
        return candidate
    }

    private fun shortDate(date: LocalDate): String {
        val weekday = date.format(DateTimeFormatter.ofPattern("EEE", spanishLocale))
            .replace(".", "")
            .replaceFirstChar { it.titlecase(spanishLocale) }
        return "$weekday ${date.dayOfMonth} ${monthShort(date)}"
    }

    private fun longDate(date: LocalDate): String {
        return date.format(
            DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", spanishLocale)
        ).replaceFirstChar { it.titlecase(spanishLocale) }
    }

    private fun monthShort(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("MMM", spanishLocale))
            .replace(".", "")
            .lowercase(spanishLocale)
    }
}
