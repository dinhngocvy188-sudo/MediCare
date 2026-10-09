```kotlin
package ui.appointment

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class SelectTimeActivity : AppCompatActivity() {

    private var doctorId: String? = null
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_time)

        doctorId = intent.getStringExtra("doctorId")
        selectedDate = intent.getStringExtra("selectedDate")

        val timeListView = findViewById<ListView>(
            R.id.timeListView
        )

        val timeSlots = arrayOf(
            "08:00 - 08:30",
            "08:30 - 09:00",
            "09:00 - 09:30",
            "09:30 - 10:00",
            "10:00 - 10:30",
            "10:30 - 11:00",
            "13:00 - 13:30",
            "13:30 - 14:00",
            "14:00 - 14:30",
            "14:30 - 15:00",
            "15:00 - 15:30",
            "15:30 - 16:00"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            timeSlots
        )

        timeListView.adapter = adapter

        timeListView.setOnItemClickListener {
                _, _, position, _ ->

            if (doctorId.isNullOrBlank() ||
                selectedDate.isNullOrBlank()
            ) {
                Toast.makeText(
                    this,
                    "Thiếu thông tin bác sĩ hoặc ngày khám.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnItemClickListener
            }

            val selectedTime = timeSlots[position]

            val intent = Intent(
                this,
                ConfirmAppointmentActivity::class.java
            )

            intent.putExtra("doctorId", doctorId)
            intent.putExtra("selectedDate", selectedDate)
            intent.putExtra("selectedTime", selectedTime)

            startActivity(intent)
        }
    }
}
```
