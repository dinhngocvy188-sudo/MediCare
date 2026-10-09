```kotlin
package ui.appointment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class ConfirmAppointmentActivity : AppCompatActivity() {

    private var doctorId: String? = null
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    private val appointmentRepository = AppointmentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_appointment)

        doctorId = intent.getStringExtra("doctorId")
        selectedDate = intent.getStringExtra("selectedDate")
        selectedTime = intent.getStringExtra("selectedTime")

        val dateText = findViewById<TextView>(R.id.dateText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val reasonInput = findViewById<EditText>(R.id.reasonInput)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        dateText.text = "Ngày khám: ${selectedDate ?: "Chưa chọn"}"
        timeText.text = "Giờ khám: ${selectedTime ?: "Chưa chọn"}"

        confirmButton.setOnClickListener {
            val doctor = doctorId
            val date = selectedDate
            val time = selectedTime
            val reason = reasonInput.text.toString().trim()

            if (doctor.isNullOrBlank() ||
                date.isNullOrBlank() ||
                time.isNullOrBlank()
            ) {
                Toast.makeText(
                    this,
                    "Thiếu thông tin lịch khám!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (reason.isBlank()) {
                reasonInput.error = "Bà nhập lý do khám giúp tui nha"
                reasonInput.requestFocus()
                return@setOnClickListener
            }

            val timeParts = time.split(" - ")

            if (timeParts.size != 2) {
                Toast.makeText(
                    this,
                    "Khung giờ khám không hợp lệ!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            confirmButton.isEnabled = false

            val appointment = Appointment(
                doctorId = doctor,
                date = date,
                startTime = timeParts[0].trim(),
                endTime = timeParts[1].trim(),
                reason = reason
            )

            appointmentRepository.addAppointment(
                appointment = appointment,
                onSuccess = { success ->
                    confirmButton.isEnabled = true

                    if (success) {
                        Toast.makeText(
                            this,
                            "Đặt lịch khám thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Giờ khám này đã có người đặt!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                onError = { message ->
                    confirmButton.isEnabled = true
                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
```
