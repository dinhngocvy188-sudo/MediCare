package ui.doctor

import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DoctorDashboardActivity : AppCompatActivity() {

    private val blue = Color.rgb(25, 118, 210)
    private val darkBlue = Color.rgb(21, 67, 120)
    private val lightBlue = Color.rgb(232, 243, 255)
    private val darkText = Color.rgb(35, 45, 60)
    private val gray = Color.rgb(105, 115, 130)

    private lateinit var appointmentContainer: LinearLayout
    private lateinit var totalCount: TextView
    private lateinit var pendingCount: TextView
    private lateinit var profileName: TextView
    private lateinit var profileSpecialty: TextView
    private lateinit var welcomeText: TextView

    private fun normalizeDoctorName(name: String): String {
        return name.trim()
            .replace(Regex("(?i)^bác sĩ\\s*"), "")
            .replace(Regex("(?i)^bs\\.?\\s*"), "")
            .trim()
    }

    private fun getCurrentDoctorName(): String {
        val prefs = getSharedPreferences("MediCareUser", MODE_PRIVATE)
        return prefs.getString("doctorName", "")?.trim() ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this).apply {
            setBackgroundColor(Color.rgb(246, 249, 253))
            isFillViewport = true
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 28)
        }

        scrollView.addView(root)

        // HEADER
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val brand = TextView(this).apply {
            text = "♥ MediCare"
            textSize = 25f
            setTextColor(blue)
            setTypeface(null, Typeface.BOLD)
        }

        header.addView(
            brand,
            LinearLayout.LayoutParams(0, -2, 1f)
        )

        root.addView(header)

        // WELCOME
        welcomeText = TextView(this).apply {
            text = "Xin chào, Bác sĩ! 👋"
            textSize = 25f
            setTypeface(null, Typeface.BOLD)
            setTextColor(darkText)
            setPadding(0, 28, 0, 6)
        }

        root.addView(welcomeText)

        val subtitle = TextView(this).apply {
            text = "Chúc bác sĩ một ngày làm việc hiệu quả."
            textSize = 14f
            setTextColor(gray)
        }

        root.addView(subtitle)

        // DOCTOR PROFILE
        val profile = makeCard().apply {
            background = GradientDrawable().apply {
                setColor(darkBlue)
                cornerRadius = 20f
            }
        }

        profileName = TextView(this).apply {
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
        }

        profileSpecialty = TextView(this).apply {
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(0, 10, 0, 0)
        }

        profile.addView(profileName)
        profile.addView(profileSpecialty)
        root.addView(profile)

        // STATISTICS
        addSectionTitle(root, "Tổng quan lịch khám hôm nay")

        val stats = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        totalCount = TextView(this).apply {
            text = "0"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(blue)
            gravity = Gravity.CENTER
        }

        pendingCount = TextView(this).apply {
            text = "0"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(230, 145, 35))
            gravity = Gravity.CENTER
        }

        stats.addView(
            makeStatCard(totalCount, "Lịch đã đặt"),
            LinearLayout.LayoutParams(0, 115, 1f).apply {
                marginEnd = 8
            }
        )

        stats.addView(
            makeStatCard(pendingCount, "Chưa xác nhận"),
            LinearLayout.LayoutParams(0, 115, 1f).apply {
                marginStart = 8
            }
        )

        root.addView(stats)

        // QUICK ACTIONS
        addSectionTitle(root, "Quản lý công việc")

        addActionButton(root, "📅   Quản lý lịch khám") {
            appointmentContainer.requestFocus()

            Toast.makeText(
                this,
                "Danh sách lịch khám nằm bên dưới.",
                Toast.LENGTH_SHORT
            ).show()
        }

        addActionButton(root, "👥   Danh sách bệnh nhân") {
            Toast.makeText(
                this,
                "Thông tin bệnh nhân hiện chưa được lưu trong dữ liệu đặt lịch.",
                Toast.LENGTH_LONG
            ).show()
        }

        addActionButton(root, "📝   Hồ sơ và ghi chú khám bệnh") {
            Toast.makeText(
                this,
                "Chức năng hồ sơ đang được phát triển.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // APPOINTMENTS
        addSectionTitle(root, "Lịch khám khách hàng đã đặt")

        appointmentContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        root.addView(appointmentContainer)

        val note = TextView(this).apply {
            text = "MediCare • Đồng hành cùng sức khỏe"
            textSize = 12f
            setTextColor(gray)
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }

        root.addView(note)

        // LOGOUT BUTTON - CUỐI TRANG
        val logout = Button(this).apply {
            text = "Đăng xuất"
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.WHITE)
            minHeight = 0
            minimumHeight = 0
            setPadding(12, 0, 12, 0)

            background = GradientDrawable().apply {
                setColor(blue)
                cornerRadius = 16f
            }

            setOnClickListener {
                getSharedPreferences("MediCareUser", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()

                val intent = android.content.Intent(
                    this@DoctorDashboardActivity,
                    ui.auth.LoginActivity::class.java
                )

                intent.flags =
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                            android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }
        }

        root.addView(
            logout,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                48
            ).apply {
                topMargin = 20
                bottomMargin = 20
            }
        )

        // INITIALIZE SCREEN ONCE
        setContentView(scrollView)

        updateDoctorProfile()
        loadAppointments()
    }

    override fun onResume() {
        super.onResume()

        if (::appointmentContainer.isInitialized) {
            updateDoctorProfile()
            loadAppointments()
        }
    }

    private fun updateDoctorProfile() {
        val doctor = getCurrentDoctorName()

        if (doctor.isBlank()) {
            profileName.text = "👨‍⚕️ Chưa xác định bác sĩ"
            profileSpecialty.text = "Vui lòng kiểm tra thông tin đăng nhập"
            welcomeText.text = "Xin chào! 👋"
            return
        }

        profileName.text = "👨‍⚕️ Bác sĩ $doctor"
        welcomeText.text = "Xin chào, Bác sĩ $doctor! 👋"

        val prefs = getSharedPreferences(
            "MediCareAppointments",
            MODE_PRIVATE
        )

        val bookings = prefs.getString("bookings", "") ?: ""

        val specialty = bookings.split("\n")
            .filter { it.isNotBlank() }
            .map { it.split("|") }
            .firstOrNull {
                it.size >= 4 &&
                        normalizeDoctorName(it[0]).equals(
                            normalizeDoctorName(doctor),
                            ignoreCase = true
                        )
            }
            ?.getOrNull(1) ?: "Chưa cập nhật"

        profileSpecialty.text = "Chuyên khoa: $specialty"
    }

    private fun loadAppointments() {
        appointmentContainer.removeAllViews()

        val currentDoctor = getCurrentDoctorName()

        if (currentDoctor.isBlank()) {
            totalCount.text = "0"
            pendingCount.text = "0"

            showEmptyMessage(
                "Không xác định được bác sĩ đang đăng nhập. Vui lòng kiểm tra LoginActivity."
            )
            return
        }

        val prefs = getSharedPreferences(
            "MediCareAppointments",
            MODE_PRIVATE
        )

        val bookings = prefs.getString("bookings", "") ?: ""

        // CHỈ LẤY LỊCH CỦA BÁC SĨ ĐANG ĐĂNG NHẬP
        val appointmentLines = bookings
            .split("\n")
            .filter { it.isNotBlank() }
            .filter { line ->
                val parts = line.split("|")

                parts.size >= 4 &&
                        normalizeDoctorName(parts[0]).equals(
                            normalizeDoctorName(currentDoctor),
                            ignoreCase = true
                        )
            }

        totalCount.text = appointmentLines.size.toString()

        // Dữ liệu hiện tại chưa có trạng thái xác nhận riêng
        pendingCount.text = appointmentLines.size.toString()

        if (appointmentLines.isEmpty()) {
            showEmptyMessage("Chưa có lịch khám nào dành cho bác sĩ này.")
            return
        }

        for (line in appointmentLines.reversed()) {
            val parts = line.split("|")
            if (parts.size < 4) continue

            val doctor = parts[0]
            val specialty = parts[1]
            val date = parts[2]
            val time = parts[3]

            val card = makeCard()

            val nameText = TextView(this).apply {
                text = "👨‍⚕️ $doctor"
                textSize = 17f
                setTypeface(null, Typeface.BOLD)
                setTextColor(darkText)
            }

            val specialtyText = TextView(this).apply {
                text = "Chuyên khoa: $specialty"
                textSize = 14f
                setTextColor(gray)
                setPadding(0, 8, 0, 0)
            }

            val dateText = TextView(this).apply {
                text = "📅 Ngày khám: $date"
                textSize = 14f
                setTextColor(darkText)
                setPadding(0, 8, 0, 0)
            }

            val timeText = TextView(this).apply {
                text = "🕒 Giờ khám: $time"
                textSize = 14f
                setTextColor(darkText)
                setPadding(0, 6, 0, 0)
            }

            val statusText = TextView(this).apply {
                text = "Đã đặt lịch"
                textSize = 13f
                setTextColor(darkBlue)
                setPadding(10, 8, 10, 8)

                background = GradientDrawable().apply {
                    setColor(lightBlue)
                    cornerRadius = 12f
                }
            }

            card.addView(nameText)
            card.addView(specialtyText)
            card.addView(dateText)
            card.addView(timeText)

            val statusRow = LinearLayout(this).apply {
                gravity = Gravity.END
                setPadding(0, 12, 0, 0)
                addView(statusText)
            }

            card.addView(statusRow)

            appointmentContainer.addView(
                card,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
            )
        }
    }

    private fun showEmptyMessage(message: String) {
        val emptyText = TextView(this).apply {
            text = message
            textSize = 15f
            setTextColor(gray)
            setPadding(0, 12, 0, 20)
        }

        appointmentContainer.addView(emptyText)
    }

    private fun makeCard(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 18, 18, 18)

            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 20f
            }

            elevation = 3f
        }
    }

    private fun addSectionTitle(
        root: LinearLayout,
        title: String
    ) {
        val text = TextView(this).apply {
            this.text = title
            textSize = 19f
            setTextColor(darkText)
            setTypeface(null, Typeface.BOLD)
            setPadding(0, 24, 0, 14)
        }

        root.addView(text)
    }

    private fun makeStatCard(
        valueView: TextView,
        label: String
    ): LinearLayout {
        val card = makeCard().apply {
            gravity = Gravity.CENTER

            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 18f
            }
        }

        val labelText = TextView(this).apply {
            text = label
            textSize = 12f
            setTextColor(gray)
            gravity = Gravity.CENTER
            setPadding(0, 6, 0, 0)
        }

        card.addView(valueView)
        card.addView(labelText)

        return card
    }

    private fun addActionButton(
        root: LinearLayout,
        title: String,
        action: () -> Unit
    ) {
        val button = Button(this).apply {
            text = title
            textSize = 14f
            isAllCaps = false
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(darkBlue)

            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 16f
                setStroke(1, Color.rgb(220, 230, 242))
            }

            setOnClickListener {
                action()
            }
        }

        root.addView(
            button,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                64
            ).apply {
                bottomMargin = 10
            }
        )
    }
}