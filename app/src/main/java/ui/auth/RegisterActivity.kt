
package ui.auth

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val confirmPassword =
            findViewById<EditText>(R.id.etConfirmPassword)
        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Các loại tài khoản
        val roles = listOf("Bệnh nhân", "Bác sĩ", "Admin")

        spinnerRole.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        ).also {
            it.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        spinnerRole.setSelection(0)

        btnRegister.setOnClickListener {
            val fullName = name.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val pass = password.text.toString()
            val confirm = confirmPassword.text.toString()
            val selectedRole = spinnerRole.selectedItem.toString()

            if (fullName.isEmpty() || userEmail.isEmpty() ||
                pass.isEmpty() || confirm.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                password.error = "Mật khẩu tối thiểu 6 ký tự"
                return@setOnClickListener
            }

            if (pass != confirm) {
                confirmPassword.error = "Mật khẩu không khớp"
                return@setOnClickListener
            }

            // Không cho tự đăng ký quyền Admin
            if (selectedRole == "Admin") {
                Toast.makeText(
                    this,
                    "Tài khoản Admin do quản trị viên cấp",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false

            auth.createUserWithEmailAndPassword(userEmail, pass)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid

                    if (uid == null) {
                        btnRegister.isEnabled = true
                        Toast.makeText(
                            this,
                            "Không lấy được UID tài khoản",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnSuccessListener
                    }

                    // Gán đúng vai trò, không cần duyệt bác sĩ
                    val accountRole = when (selectedRole) {
                        "Bác sĩ" -> "doctor"
                        else -> "patient"
                    }

                    val userData = hashMapOf(
                        "name" to fullName,
                        "email" to userEmail,
                        "role" to accountRole,
                        "status" to "active"
                    )

                    db.collection("users")
                        .document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Đăng ký thành công! Hãy đăng nhập.",
                                Toast.LENGTH_LONG
                            ).show()

                            auth.signOut()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            btnRegister.isEnabled = true
                            Toast.makeText(
                                this,
                                "Lỗi lưu hồ sơ: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    btnRegister.isEnabled = true
                    Toast.makeText(
                        this,
                        "Đăng ký thất bại: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}
