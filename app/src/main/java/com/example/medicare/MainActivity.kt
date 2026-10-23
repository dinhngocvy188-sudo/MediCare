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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            openPage(LoginActivity::class.java)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (isFinishing || isDestroyed) return@addOnSuccessListener

                val role = document.getString("role")
                    ?.trim()
                    ?.lowercase()

                Log.d("MainActivity", "Role = $role")

                val target = when (role) {
                    "admin" -> AdminDashboardActivity::class.java
                    "doctor" -> DoctorDashboardActivity::class.java
                    "patient" -> PatientHomeActivity::class.java
                    else -> null
                }

                if (target != null) {
                    openPage(target)
                } else {
                    Toast.makeText(
                        this,
                        "Không tìm thấy vai trò tài khoản. Kiểm tra dữ liệu users trong Firestore.",
                        Toast.LENGTH_LONG
                    ).show()
                    auth.signOut()
                    openPage(LoginActivity::class.java)
                }
            }
            .addOnFailureListener { error ->
                Log.e("MainActivity", "Firestore error", error)
                Toast.makeText(
                    this,
                    "Không tải được thông tin tài khoản: ${error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                auth.signOut()
                openPage(LoginActivity::class.java)
            }
    }

    private fun openPage(activity: Class<*>) {
        startActivity(Intent(this, activity))
        finish()
    }
}
