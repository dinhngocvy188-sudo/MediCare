
package ui.appointment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class MyAppointmentsActivity : AppCompatActivity() {

    private val appointmentRepository = AppointmentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)

        val appointmentListView =
            findViewById<ListView>(R.id.appointmentListView)

        val emptyText =
            findViewById<TextView>(R.id.emptyText)

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )

        appointmentListView.adapter = adapter
        appointmentListView.emptyView = emptyText

        loadAppointments(adapter)
    }

    private fun loadAppointments(adapter: ArrayAdapter<String>) {
        appointmentRepository.getAppointments(
            onSuccess = { appointments ->
                val displayItems = appointments.map { appointment ->
                    "Ngày khám: ${appointment.date}\n" +
                    "Giờ khám: ${appointment.startTime}\n" +
                    "Bác sĩ: ${appointment.doctorId}\n" +
                    "Trạng thái: ${appointment.status}"
                }

                adapter.clear()
                adapter.addAll(displayItems)
                adapter.notifyDataSetChanged()
            },
            onError = { message ->
                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }
}
