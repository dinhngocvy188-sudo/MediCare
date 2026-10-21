
package ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.google.firebase.auth.FirebaseAuth
import ui.auth.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Quản lý bác sĩ
        findViewById<Button>(R.id.btnManageDoctors)
            .setOnClickListener {
                Toast.makeText(
                    this,
                    "Chức năng quản lý bác sĩ đang được xây dựng",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // Quản lý bệnh nhân
        findViewById<Button>(R.id.btnManagePatients)
            .setOnClickListener {
                Toast.makeText(
                    this,
                    "Chức năng quản lý bệnh nhân đang được xây dựng",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // Quản lý lịch hẹn
        findViewById<Button>(R.id.btnManageAppointments)
            .setOnClickListener {
                Toast.makeText(
                    this,
                    "Chức năng quản lý lịch hẹn đang được xây dựng",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // Quản lý tài khoản
        findViewById<Button>(R.id.btnManageAccounts)
            .setOnClickListener {
                Toast.makeText(
                    this,
                    "Chức năng quản lý tài khoản đang được xây dựng",
                    Toast.LENGTH_SHORT
                ).show()
            }

        // Đăng xuất Firebase
        findViewById<Button>(R.id.btnAdminLogout)
            .setOnClickListener {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }
    }
}
