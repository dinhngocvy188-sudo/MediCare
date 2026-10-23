
package ui.patient

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.graphics.Color
import android.widget.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var doctorName: String
    private lateinit var specialty: String
    private lateinit var selectedDate: TextView
    private lateinit var selectedTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doctorName = intent.getStringExtra("doctorName") ?: "Bác sĩ"
        specialty = intent.getStringExtra("specialty") ?: "Chưa cập nhật"

        createBookingScreen()
    }

    private fun createBookingScreen() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(245, 248, 252))
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val title = TextView(this).apply {
            text = "Thông tin đặt lịch"
            textSize = 26f
            setTextColor(Color.rgb(25, 118, 210))
            setPadding(0, 0, 0, 24)
        }
        content.addView(title)

        val doctorInfo = TextView(this).apply {
            text = "$doctorName\nChuyên khoa: $specialty"
            textSize = 19f
            setTextColor(Color.DKGRAY)
            setPadding(20, 20, 20, 20)
            setBackgroundColor(Color.WHITE)
        }
        content.addView(doctorInfo)

        content.addView(makeLabel("Ngày khám"))
        selectedDate = makeValue("Chưa chọn ngày")

        val dateButton = Button(this).apply {
            text = "Chọn ngày khám"
            isAllCaps = false
            setOnClickListener { chooseDate() }
        }
        content.addView(selectedDate)
        content.addView(dateButton)

        content.addView(makeLabel("Giờ khám"))
        selectedTime = makeValue("Chưa chọn giờ")

        val timeButton = Button(this).apply {
            text = "Chọn giờ khám"
            isAllCaps = false
            setOnClickListener { chooseTime() }
        }
        content.addView(selectedTime)
        content.addView(timeButton)

        val bookButton = Button(this).apply {
            text = "XÁC NHẬN ĐẶT LỊCH"
            isAllCaps = false
            setOnClickListener { confirmBooking() }
        }
        content.addView(bookButton)

        val historyButton = Button(this).apply {
            text = "Lịch khám của tôi"
            isAllCaps = false
            setOnClickListener {
                startActivity(
                    Intent(
                        this@BookingActivity,
                        PatientAppointmentsActivity::class.java
                    )
                )
            }
        }
        content.addView(historyButton)

        scroll.addView(content)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun makeLabel(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 18f
            setTextColor(Color.BLACK)
            setPadding(0, 24, 0, 8)
        }
    }

    private fun makeValue(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 16f
            setTextColor(Color.DKGRAY)
            setPadding(0, 8, 0, 8)
        }
    }

    private fun chooseDate() {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedDate.text = String.format(
                    Locale.getDefault(),
                    "%02d/%02d/%04d",
                    day,
                    month + 1,
                    year
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
        }.show()
    }

    private fun chooseTime() {
        val calendar = Calendar.getInstance()

        TimePickerDialog(
            this,
            { _, hour, minute ->
                selectedTime.text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    hour,
                    minute
                )
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun confirmBooking() {
        val date = selectedDate.text.toString()
        val time = selectedTime.text.toString()

        if (date == "Chưa chọn ngày" || time == "Chưa chọn giờ") {
            Toast.makeText(
                this,
                "Bà hãy chọn ngày và giờ khám nha!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // LƯU THÔNG TIN LỊCH KHÁM, BAO GỒM CẢ GIỜ ĐÃ CHỌN
        val appointment = getSharedPreferences(
            "MediCareAppointments",
            MODE_PRIVATE
        )

        val oldBookings =
            appointment.getString("bookings", "") ?: ""

        val newBooking = "$doctorName|$specialty|$date|$time"

        appointment.edit()
            .putString(
                "bookings",
                if (oldBookings.isEmpty()) {
                    newBooking
                } else {
                    "$oldBookings\n$newBooking"
                }
            )
            .apply()

        Toast.makeText(
            this,
            "Đặt lịch thành công!",
            Toast.LENGTH_LONG
        ).show()

        startActivity(
            Intent(
                this@BookingActivity,
                PatientAppointmentsActivity::class.java
            )
        )
    }
}
