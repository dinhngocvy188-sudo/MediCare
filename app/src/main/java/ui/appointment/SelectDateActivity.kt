package ui.appointment

import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import java.util.Calendar

class SelectDateActivity : AppCompatActivity() {

    private var doctorId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)

        doctorId = intent.getStringExtra("doctorId")

        val datePicker = findViewById<DatePicker>(R.id.datePicker)

        val today = Calendar.getInstance()
        datePicker.minDate = today.timeInMillis

        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            Toast.makeText(
                this,
                "Đã chọn ngày: $dayOfMonth/${month + 1}/$year",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
