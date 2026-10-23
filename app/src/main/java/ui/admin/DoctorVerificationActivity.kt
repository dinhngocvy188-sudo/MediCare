
package ui.admin

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class DoctorVerificationActivity : Activity() {

    private val prefsName = "MediCareDoctors"
    private val doctorsKey = "doctors"
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val title = TextView(this).apply {
            text = "XÁC THỰC BÁC SĨ"
            textSize = 24f
            gravity = Gravity.CENTER
        }
        root.addView(title)

        val refresh = Button(this).apply {
            text = "Làm mới danh sách"
            setOnClickListener { loadDoctors() }
        }
        root.addView(refresh)

        container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scroll = ScrollView(this)
        scroll.addView(container)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        loadDoctors()
    }

    private fun loadDoctors() {
        container.removeAllViews()

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val doctors = try {
            JSONArray(prefs.getString(doctorsKey, "[]") ?: "[]")
        } catch (e: Exception) {
            JSONArray()
        }

        if (doctors.length() == 0) {
            container.addView(TextView(this).apply {
                text = "Chưa có dữ liệu bác sĩ."
                textSize = 18f
                setPadding(0, 24, 0, 24)
            })
            return
        }

        for (i in 0 until doctors.length()) {
            val doctor = doctors.optJSONObject(i) ?: continue
            val name = doctor.optString("name", "Chưa có tên")
            val email = doctor.optString("email", "")
            val verified = doctor.optBoolean("verified", false)

            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
            }

            card.addView(TextView(this).apply {
                text = "Bác sĩ: $name\nEmail: $email\nTrạng thái: ${
                    if (verified) "Đã xác thực" else "Chờ xác thực"
                }"
                textSize = 17f
            })

            if (!verified) {
                card.addView(Button(this).apply {
                    text = "Xác thực bác sĩ"
                    setOnClickListener {
                        doctor.put("verified", true)
                        doctors.put(i, doctor)

                        val saved = prefs.edit()
                            .putString(doctorsKey, doctors.toString())
                            .commit()

                        if (saved) {
                            Toast.makeText(
                                this@DoctorVerificationActivity,
                                "Đã xác thực $name",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadDoctors()
                        } else {
                            Toast.makeText(
                                this@DoctorVerificationActivity,
                                "Lưu trạng thái thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }

            container.addView(card)
            container.addView(TextView(this).apply {
                text = "────────────────────"
            })
        }
    }
}
