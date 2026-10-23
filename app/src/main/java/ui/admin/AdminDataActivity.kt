
package ui.admin

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminDataActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterRole = intent.getStringExtra("filterRole") ?: "all"

        val title = when (filterRole) {
            "doctor" -> "Quản lý bác sĩ"
            "patient" -> "Quản lý bệnh nhân"
            "appointments" -> "Quản lý lịch hẹn"
            else -> "Quản lý tài khoản"
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 24, 32, 24)
        }

        val heading = TextView(this).apply {
            text = title
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 24)
        }

        root.addView(heading)

        val scroll = ScrollView(this)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)

        when (filterRole) {
            "appointments" -> loadAppointments()
            else -> loadUsers(filterRole)
        }
    }

    // QUẢN LÝ BÁC SĨ, BỆNH NHÂN VÀ TÀI KHOẢN
    private fun loadUsers(filterRole: String) {
        content.removeAllViews()

        val loading = ProgressBar(this)
        content.addView(loading)

        db.collection("users").get()
            .addOnSuccessListener { result ->
                content.removeAllViews()

                val users = result.documents.filter { document ->
                    val role = document.getString("role")
                        ?.trim()
                        ?.lowercase() ?: ""

                    filterRole == "all" || role == filterRole
                }

                if (users.isEmpty()) {
                    showMessage(
                        "Chưa có dữ liệu phù hợp trong collection users."
                    )
                    return@addOnSuccessListener
                }

                users.forEach { document ->
                    val name = document.getString("name")
                        ?: document.getString("fullName")
                        ?: "Chưa có tên"

                    val email = document.getString("email")
                        ?: "Chưa có email"

                    val role = document.getString("role")
                        ?: "Chưa có vai trò"

                    val status = document.getString("status")
                        ?: "Chưa có trạng thái"

                    addItem(
                        """
                        Họ tên: $name
                        Email: $email
                        Vai trò: $role
                        Trạng thái: $status
                        """.trimIndent()
                    )
                }
            }
            .addOnFailureListener { error ->
                showMessage(
                    "Không tải được dữ liệu: ${error.localizedMessage}"
                )
            }
    }

    // QUẢN LÝ LỊCH HẸN ĐANG LƯU TRÊN THIẾT BỊ
    private fun loadAppointments() {
        content.removeAllViews()

        val prefs = getSharedPreferences(
            "MediCareAppointments",
            MODE_PRIVATE
        )

        val bookings = prefs.getString("bookings", "") ?: ""

        if (bookings.isBlank()) {
            showMessage(
                "Chưa có lịch hẹn được lưu trên thiết bị này.\n\n" +
                        "Hãy đặt thử một lịch hẹn rồi mở lại màn hình."
            )
            return
        }

        val appointmentList = bookings
            .split("\n")
            .filter { it.isNotBlank() }

        appointmentList.forEachIndexed { index, booking ->
            val parts = booking.split("|")

            val info = if (parts.size >= 4) {
                """
                LỊCH HẸN ${index + 1}

                Bác sĩ: ${parts[0]}
                Chuyên khoa: ${parts[1]}
                Ngày khám: ${parts[2]}
                Giờ khám: ${parts[3]}
                """.trimIndent()
            } else {
                "Lịch hẹn ${index + 1}: $booking"
            }

            addItem(info)
        }
    }

    // HIỂN THỊ TỪNG MỤC DỮ LIỆU
    private fun addItem(info: String) {
        val item = TextView(this).apply {
            text = info
            textSize = 16f
            setPadding(16, 20, 16, 20)
        }

        content.addView(item)

        val divider = View(this).apply {
            setBackgroundColor(0xFFDDDDDD.toInt())
        }

        content.addView(
            divider,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
        )
    }

    // HIỂN THỊ THÔNG BÁO
    private fun showMessage(message: String) {
        content.removeAllViews()

        val text = TextView(this).apply {
            this.text = message
            textSize = 16f
            setPadding(8, 24, 8, 24)
        }

        content.addView(text)
    }
}
