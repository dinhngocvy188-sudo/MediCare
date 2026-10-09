```kotlin
// Hủy lịch khám, chỉ cho phép bệnh nhân hủy lịch của mình
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

    appointmentsCollection
        .document(appointmentId)
        .get()
        .addOnSuccessListener { document ->

            if (!document.exists()) {
                onError("Không tìm thấy lịch khám!")
                return@addOnSuccessListener
            }

            val patientId = document.getString("patientId")
            val status = document.getString("status")

            if (patientId != uid) {
                onError("Bà không có quyền hủy lịch khám này!")
                return@addOnSuccessListener
            }

            if (status == "CANCELLED") {
                onError("Lịch khám này đã được hủy trước đó!")
                return@addOnSuccessListener
            }

            document.reference
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
        .addOnFailureListener { exception ->
            onError(
                exception.localizedMessage
                    ?: "Không thể kiểm tra lịch khám!"
            )
        }
}
```
