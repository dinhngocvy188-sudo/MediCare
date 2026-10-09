```kotlin
package ui.appointment

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class ConfirmAppointmentActivity : AppCompatActivity() {

    private var doctorId: String? = null
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_appointment)

        doctorId = intent.getStringExtra("doctorId")
        selectedDate = intent.getStringExtra("selectedDate")
        selectedTime = intent.getStringExtra("selectedTime")

        val dateText = findViewById<TextView>(R.id.dateText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        dateText.text = "Ngày khám: ${selectedDate ?: "Chưa chọn"}"
        timeText.text = "Giờ khám: ${selectedTime ?: "Chưa chọn"}"

        confirmButton.setOnClickListener {
            if (doctorId.isNullOrBlank() ||
                selectedDate.isNullOrBlank() ||
                selectedTime.isNullOrBlank()
            ) {
                Toast.makeText(
                    this,
                    "Thiếu thông tin lịch khám!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Thông tin hợp lệ. Bước tiếp theo sẽ lưu lịch khám.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
```
