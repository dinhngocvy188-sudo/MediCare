package com.example.medicare.repository

import com.example.medicare.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }

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

    fun logout() {
        auth.signOut()
    }

    fun fetchUserData(onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val uid = getCurrentUid()
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onError("Không tìm thấy dữ liệu người dùng!")
                    }
                }
                .addOnFailureListener { exception ->
                    onError(exception.localizedMessage ?: "Lỗi lấy thông tin người dùng")
                }
        } else {
            onError("Người dùng chưa đăng nhập!")
        }
    }
}