```kotlin
package ui.appointment

data class Appointment(
    val appointmentId: String = "",
    val patientId: String = "",
    val doctorId: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val reason: String = "",
    val status: String = "PENDING",
    val paymentStatus: String = "UNPAID"
)

class AppointmentRepository {

    // Danh sách tạm thời trong bộ nhớ.
    // Sau này sẽ thay bằng dữ liệu từ Cloud Firestore.
    private val appointments = mutableListOf<Appointment>()

    fun getAppointments(): List<Appointment> {
        return appointments.toList()
    }

    fun addAppointment(appointment: Appointment): Boolean {
        val isDuplicate = appointments.any {
            it.doctorId == appointment.doctorId &&
            it.date == appointment.date &&
            it.startTime == appointment.startTime &&
            it.status != "CANCELLED"
        }

        if (isDuplicate) {
            return false
        }

        appointments.add(appointment)
        return true
    }

    fun cancelAppointment(appointmentId: String): Boolean {
        val index = appointments.indexOfFirst {
            it.appointmentId == appointmentId
        }

        if (index == -1) {
            return false
        }

        appointments[index] = appointments[index].copy(
            status = "CANCELLED"
        )

        return true
    }
}
```
