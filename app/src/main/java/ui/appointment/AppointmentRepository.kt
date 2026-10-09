
package ui.appointment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val appointmentsCollection
        get() = db.collection("appointments")

    // Lấy danh sách lịch khám của bệnh nhân đang đăng nhập
    fun getAppointments(
        onSuccess: (List<Appointment>) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Người dùng chưa đăng nhập!")
            return
        }

        appointmentsCollection
            .whereEqualTo("patientId", uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val appointments = snapshot.documents.mapNotNull { document ->
                    document.toObject(Appointment::class.java)
                        ?.copy(appointmentId = document.id)
                }

                onSuccess(appointments)
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Không thể tải danh sách lịch khám!"
                )
            }
    }

    // Thêm lịch khám lên Firestore, đồng thời kiểm tra trùng giờ
    fun addAppointment(
        appointment: Appointment,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onError("Người dùng chưa đăng nhập!")
            return
        }

        val patientId = appointment.patientId.ifBlank { uid }

        appointmentsCollection
            .whereEqualTo("doctorId", appointment.doctorId)
            .whereEqualTo("date", appointment.date)
            .get()
            .addOnSuccessListener { snapshot ->

                val isDuplicate = snapshot.documents.any { document ->
                    val existing =
                        document.toObject(Appointment::class.java)

                    existing != null &&
                    existing.startTime == appointment.startTime &&
                    existing.status != "CANCELLED"
                }

                if (isDuplicate) {
                    onSuccess(false)
                    return@addOnSuccessListener
                }

                val document = appointmentsCollection.document()

                val newAppointment = appointment.copy(
                    appointmentId = document.id,
                    patientId = patientId
                )

                document.set(newAppointment)
                    .addOnSuccessListener {
                        onSuccess(true)
                    }
                    .addOnFailureListener { exception ->
                        onError(
                            exception.localizedMessage
                                ?: "Không thể lưu lịch khám!"
                        )
                    }
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Không thể kiểm tra lịch khám trùng!"
                )
            }
    }

    // Hủy lịch khám trên Firestore
    fun cancelAppointment(
        appointmentId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (appointmentId.isBlank()) {
            onError("Mã lịch khám không hợp lệ!")
            return
        }

        appointmentsCollection
            .document(appointmentId)
            .update("status", "CANCELLED")
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Không thể hủy lịch khám!"
                )
            }
    }
}
