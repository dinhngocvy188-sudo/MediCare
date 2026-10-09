```kotlin
package ui.appointment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class MyAppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)

        val appointmentListView =
            findViewById<ListView>(R.id.appointmentListView)

        val emptyText =
            findViewById<TextView>(R.id.emptyText)

        // Danh sách mẫu để kiểm tra giao diện.
        // Sau này sẽ thay bằng dữ liệu đọc từ Firestore.
        val appointments = arrayOf<String>()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            appointments
        )

        appointmentListView.adapter = adapter
        appointmentListView.emptyView = emptyText
    }
}
```
