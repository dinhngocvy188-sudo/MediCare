
package ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ui.auth.LoginActivity

class AdminDashboardActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        findViewById<Button>(R.id.btnManageDoctors)
            .setOnClickListener {
                openAdminData("doctor")
            }

        findViewById<Button>(R.id.btnManagePatients)
            .setOnClickListener {
                openAdminData("patient")
            }

        findViewById<Button>(R.id.btnManageAppointments)
            .setOnClickListener {
                openAdminData("appointments")
            }

        findViewById<Button>(R.id.btnManageAccounts)
            .setOnClickListener {
                openAdminData("all")
            }

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

        loadSystemOverview()
    }

    private fun loadSystemOverview() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                // Đếm bác sĩ
                val totalDoctors = result.documents.count {
                    it.getString("role")
                        ?.trim()
                        ?.equals("doctor", ignoreCase = true) == true
                }

                // Đếm bệnh nhân
                val totalPatients = result.documents.count {
                    it.getString("role")
                        ?.trim()
                        ?.equals("patient", ignoreCase = true) == true
                }

                // Đếm lịch hẹn trên thiết bị hiện tại
                val prefs = getSharedPreferences(
                    "MediCareAppointments",
                    MODE_PRIVATE
                )

                val bookings = prefs.getString("bookings", "") ?: ""

                val totalAppointments = bookings
                    .lines()
                    .count { it.isNotBlank() }

                // Hiển thị số liệu lên giao diện
                findViewById<TextView>(R.id.tvTotalDoctors)
                    .text = totalDoctors.toString()

                findViewById<TextView>(R.id.tvTotalPatients)
                    .text = totalPatients.toString()

                findViewById<TextView>(R.id.tvTotalAppointments)
                    .text = totalAppointments.toString()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Không tải được tổng quan hệ thống",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun openAdminData(filterRole: String) {
        val intent = Intent(this, AdminDataActivity::class.java)
        intent.putExtra("filterRole", filterRole)
        startActivity(intent)
    }
}
