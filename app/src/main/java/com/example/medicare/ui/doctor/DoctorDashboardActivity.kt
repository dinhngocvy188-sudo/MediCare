package com.example.medicare.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.example.medicare.repository.AuthRepository
import com.example.medicare.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class DoctorDashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnDoctorList: Button
    private lateinit var btnLogout: Button

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_dashboard)

        initViews()
        loadDoctorInfo()
        setupListeners()
    }

    private fun initViews() {
        tvWelcome = findViewById(R.id.tvWelcome)
        btnDoctorList = findViewById(R.id.btnDoctorList)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun loadDoctorInfo() {
        authRepository.fetchUserData(
            onSuccess = { user ->
                tvWelcome.text = getString(R.string.welcome_user_format, user.fullName)
            },
        ) { errorMessage ->
            tvWelcome.text = getString(R.string.welcome_doctor)
            Toast.makeText(
                this,
                errorMessage,
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun setupListeners() {
        btnDoctorList.setOnClickListener {
            startActivity(
                Intent(this, DoctorListActivity::class.java),
            )
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            Toast.makeText(
                this,
                "Đã đăng xuất",
                Toast.LENGTH_SHORT,
            ).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
