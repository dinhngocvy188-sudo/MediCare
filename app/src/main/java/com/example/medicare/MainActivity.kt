package com.example.medicare

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.repository.AuthRepository
import com.example.medicare.ui.auth.LoginActivity
import com.example.medicare.ui.doctor.DoctorListActivity

class MainActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkUserSession()
    }

    private fun checkUserSession() {
        val uid = authRepository.getCurrentUid()
        if (uid == null) {
            navigateToLogin()
        } else {
            navigateToDoctorList()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToDoctorList() {
        val intent = Intent(this, DoctorListActivity::class.java)
        startActivity(intent)
        finish()
    }
}