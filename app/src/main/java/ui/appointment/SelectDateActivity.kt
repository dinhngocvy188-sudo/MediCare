```kotlin
package ui.appointment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import java.util.Calendar
import java.util.Locale

class SelectDateActivity : AppCompatActivity() {

    private var doctorId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)

        doctorId = intent.getStringExtra("doctorId")

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val continueButton = findViewById<Button>(R.id.continueButton)

        val today = Calendar.getInstance()
        datePicker.minDate = today.timeInMillis

        continueButton.setOnClickListener {
            val year = datePicker.year
            val month = datePicker.month + 1
            val day = datePicker.dayOfMonth

            val selectedDate = String.format(
                Locale.US,
                "%04d-%02d-%02d",
                year,
                month,
                day
            )

            if (doctorId.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    "Chưa nhận được thông tin bác sĩ.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SelectTimeActivity::class.java)
            intent.putExtra("doctorId", doctorId)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }
    }
}
```
