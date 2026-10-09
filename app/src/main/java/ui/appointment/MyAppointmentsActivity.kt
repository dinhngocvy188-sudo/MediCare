```kotlin
package ui.appointment

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        appointmentListView.setOnItemClickListener { _, _, position, _ ->
            val appointments = currentAppointments

            if (position !in appointments.indices) {
                return@setOnItemClickListener
            }

            val appointment = appointments[position]

            if (appointment.status == "CANCELLED") {
                Toast.makeText(
                    this,
                    "Lịch khám này đã được hủy rồi bà.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnItemClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy lịch")
                .setMessage(
                    "Bà có chắc muốn hủy lịch khám ngày " +
                    "${appointment.date}, lúc ${appointment.startTime} không?"
                )
                .setNegativeButton("Không", null)
                .setPositiveButton("Hủy lịch") { _, _ ->
                    appointmentRepository.cancelAppointment(
                        appointmentId = appointment.appointmentId,
                        onSuccess = {
                            Toast.makeText(
                                this,
                                "Đã hủy lịch khám thành công!",
                                Toast.LENGTH_SHORT
                            ).show()

                            loadAppointments(adapter)
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
                .show()
        }
    }

    private var currentAppointments: List<Appointment> = emptyList()

    private fun loadAppointments(adapter: ArrayAdapter<String>) {
        appointmentRepository.getAppointments(
            onSuccess = { appointments ->
                currentAppointments = appointments

                val displayItems = appointments.map { appointment ->
                    "Ngày khám: ${appointment.date}\n" +
                    "Giờ khám: ${appointment.startTime} - ${appointment.endTime}\n" +
                    "Bác sĩ: ${appointment.doctorId}\n" +
                    "Trạng thái: ${appointment.status}\n" +
                    "Nhấn vào lịch để hủy nếu cần"
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
```
