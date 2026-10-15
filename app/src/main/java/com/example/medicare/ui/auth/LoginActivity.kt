package com.example.medicare.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.example.medicare.repository.AuthRepository
import com.example.medicare.ui.doctor.DoctorListActivity

class LoginActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_login2)
        } catch (e: Exception) {
            setContentView(R.layout.activity_login)
        }

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin?.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ Email và Mật khẩu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authRepository.loginUser(
                email = email,
                password = password,
                onSuccess = {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DoctorListActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, "Đăng nhập thất bại: $errorMessage", Toast.LENGTH_LONG).show()
                }
            )
        }

        btnRegister?.setOnClickListener {
            Toast.makeText(this, "Chuyển sang giao diện đăng ký", Toast.LENGTH_SHORT).show()
        }
    }
}