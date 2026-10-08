package ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.MainActivity
import com.example.medicare.R
import com.example.medicare.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private val authRepository by lazy { AuthRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnForgotPassword = findViewById<Button>(R.id.btnForgotPassword)

        // =========================
        // ĐĂNG NHẬP
        // =========================
        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ Email và Mật khẩu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            authRepository.loginUser(
                email,
                password,

                onSuccess = {
                    Toast.makeText(
                        this,
                        "Đăng nhập thành công!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    )

                    startActivity(intent)
                    finish()
                },

                onError = { errorMessage ->
                    Toast.makeText(
                        this,
                        "Đăng nhập thất bại: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }

        // =========================
        // QUÊN MẬT KHẨU
        // =========================
        btnForgotPassword.setOnClickListener {

            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Vui lòng nhập Email trước",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Đã gửi email đặt lại mật khẩu. Vui lòng kiểm tra hộp thư!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { exception ->

                    Toast.makeText(
                        this,
                        "Không thể gửi email: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}