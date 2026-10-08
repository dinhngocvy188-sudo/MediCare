package com.example.medicare.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import model.User

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Lấy UID của user đang đăng nhập
    fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }

    // Hàm xử lý đăng nhập bằng Email và Mật khẩu
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.localizedMessage ?: "Đăng nhập thất bại!")
            }
    }

    // Lấy thông tin User từ Firestore
    fun fetchUserData(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = getCurrentUid()
        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            onSuccess(user)
                        } else {
                            onError("Không thể đọc dữ liệu người dùng!")
                        }
                    } else {
                        onError("Không tìm thấy thông tin người dùng trên hệ thống!")
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception.localizedMessage ?: "Lỗi tải dữ liệu người dùng!")
                }
        } else {
            onError("Người dùng chưa đăng nhập!")
        }
    }
}