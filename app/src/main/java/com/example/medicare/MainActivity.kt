package com.example.medicare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ui.admin.AdminDashboardActivity
import ui.auth.LoginActivity
import ui.doctor.DoctorDashboardActivity
import ui.patient.PatientHomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        checkUserRoleAndNavigate()
    }

    private fun checkUserRoleAndNavigate() {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            navigateToActivity(LoginActivity::class.java)
            return
        }

        val uid = currentUser.uid

        Log.d("MainActivity", "Current UID: $uid")

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val role = document.getString("role")

                    Log.d(
                        "MainActivity",
                        "User role: $role"
                    )

                    when (role?.lowercase()) {

                        "admin" -> {
                            navigateToActivity(
                                AdminDashboardActivity::class.java
                            )
                        }

                        "doctor" -> {
                            navigateToActivity(
                                DoctorDashboardActivity::class.java
                            )
                        }

                        "patient" -> {
                            navigateToActivity(
                                PatientHomeActivity::class.java
                            )
                        }

                        else -> {
                            Toast.makeText(
                                this,
                                "Tài khoản chưa được phân quyền!",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToActivity(
                                LoginActivity::class.java
                            )
                        }
                    }

                } else {

                    Log.e(
                        "MainActivity",
                        "Không tìm thấy user với UID: $uid"
                    )

                    Toast.makeText(
                        this,
                        "Không tìm thấy thông tin tài khoản!",
                        Toast.LENGTH_LONG
                    ).show()

                    navigateToActivity(
                        LoginActivity::class.java
                    )
                }
            }
            .addOnFailureListener { exception ->

                Log.e(
                    "MainActivity",
                    "Lỗi Firestore",
                    exception
                )

                Toast.makeText(
                    this,
                    "Không thể kết nối Firestore: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun navigateToActivity(
        targetActivity: Class<*>
    ) {
        try {

            val intent = Intent(
                this,
                targetActivity
            )

            startActivity(intent)
            finish()

        } catch (e: Exception) {

            Log.e(
                "MainActivity",
                "Không thể mở Activity: ${targetActivity.simpleName}",
                e
            )

            Toast.makeText(
                this,
                "Lỗi mở màn hình: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}