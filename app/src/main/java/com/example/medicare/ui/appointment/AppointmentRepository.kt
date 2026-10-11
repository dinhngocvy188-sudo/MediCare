
package com.example.medicare.ui.appointment

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

    // Tải danh sách lịch khám của bệnh nhân đang đăng nhập
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
                onError(exception.localizedMessage
                    ?: "Không thể tải danh sách lịch khám!")
            }
    }

    // Thêm lịch khám mới
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

        if (appointment.doctorId.isBlank() ||
            appointment.date.isBlank() ||
            appointment.startTime.isBlank() ||
            appointment.endTime.isBlank() ||
            appointment.reason.isBlank()
        ) {
            onError("Thông tin đặt lịch chưa đầy đủ!")
            return
        }

        // Kiểm tra lịch đã tồn tại ở cùng bác sĩ, ngày và giờ bắt đầu
        appointmentsCollection
            .whereEqualTo("doctorId", appointment.doctorId)
            .whereEqualTo("date", appointment.date)
            .get()
            .addOnSuccessListener { snapshot ->
                val isDuplicate = snapshot.documents.any { document ->
                    val existing = document.toObject(Appointment::class.java)
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
                    patientId = uid,
                    status = "PENDING",
                    paymentStatus = "UNPAID"
                )

                document.set(newAppointment)
                    .addOnSuccessListener { onSuccess(true) }
                    .addOnFailureListener { exception ->
                        onError(exception.localizedMessage
                            ?: "Không thể lưu lịch khám!")
                    }
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage
                    ?: "Không thể kiểm tra lịch khám trùng!")
            }
    }

    // Hủy lịch khám của chính bệnh nhân
    fun cancelAppointment(
        appointmentId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onError("Người dùng chưa đăng nhập!")
            return
        }

        if (appointmentId.isBlank()) {
            onError("Mã lịch khám không hợp lệ!")
            return
        }

        appointmentsCollection.document(appointmentId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    onError("Không tìm thấy lịch khám!")
                    return@addOnSuccessListener
                }

                if (document.getString("patientId") != uid) {
                    onError("Bà không có quyền hủy lịch khám này!")
                    return@addOnSuccessListener
                }

                if (document.getString("status") == "CANCELLED") {
                    onError("Lịch khám này đã được hủy trước đó!")
                    return@addOnSuccessListener
                }

                document.reference.update("status", "CANCELLED")
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { exception ->
                        onError(exception.localizedMessage
                            ?: "Không thể hủy lịch khám!")
                    }
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage
                    ?: "Không thể kiểm tra lịch khám!")
            }
    }
}
