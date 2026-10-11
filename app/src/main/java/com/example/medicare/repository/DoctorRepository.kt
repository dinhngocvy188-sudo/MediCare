package com.example.medicare.repository

import com.example.medicare.model.Doctor
import com.google.firebase.firestore.FirebaseFirestore

class DoctorRepository {

    private val db = FirebaseFirestore.getInstance()

    // Danh sách bác sĩ mẫu hiển thị ngay tức thì
    private val mockDoctorList = listOf(
        Doctor(
            id = "doc_1",
            name = "BS. Nguyễn Văn An",
            specialty = "Chuyên khoa Tim mạch",
            experience = "12 năm kinh nghiệm",
            rating = 4.9,
            hospital = "Bệnh viện Đại học Y Dược",
            phone = "0901234567",
        ),
        Doctor(
            id = "doc_2",
            name = "BS. Trần Thị Mai",
            specialty = "Chuyên khoa Nhi",
            experience = "8 năm kinh nghiệm",
            rating = 4.8,
            hospital = "Bệnh viện Nhi Đồng 1",
            phone = "0912345678",
        ),
        Doctor(
            id = "doc_3",
            name = "BS. Lê Hoàng Nam",
            specialty = "Chuyên khoa Thần kinh",
            experience = "15 năm kinh nghiệm",
            rating = 5.0,
            hospital = "Bệnh viện Chợ Rẫy",
            phone = "0923456789",
        ),
        Doctor(
            id = "doc_4",
            name = "BS. Phạm Minh Tuấn",
            specialty = "Chuyên khoa Đa khoa",
            experience = "6 năm kinh nghiệm",
            rating = 4.7,
            hospital = "Bệnh viện Bạch Mai",
            phone = "0934567890",
        ),
    )

    fun getDoctors(onSuccess: (List<Doctor>) -> Unit, onFailure: (Exception) -> Unit) {
        // Lấy dữ liệu từ Firebase
        db.collection("doctors")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val firebaseDoctors = querySnapshot.toObjects(Doctor::class.java)

                if (firebaseDoctors.isNotEmpty()) {
                    // Nếu trên Firebase có dữ liệu -> Hiển thị từ Firebase
                    onSuccess(firebaseDoctors)
                } else {
                    // Nếu Firebase chưa có dữ liệu -> Hiện ngay danh sách bác sĩ mẫu
                    onSuccess(mockDoctorList)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                // Nếu gặp lỗi kết nối/mạng -> Vẫn hiện danh sách bác sĩ mẫu
                onSuccess(mockDoctorList)
            }
    }
}
