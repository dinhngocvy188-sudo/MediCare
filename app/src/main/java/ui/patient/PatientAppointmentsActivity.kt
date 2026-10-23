
package ui.patient

import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PatientAppointmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(245, 248, 252))
        }

        val title = TextView(this).apply {
            text = "Lịch khám của tôi"
            textSize = 24f
            setTextColor(Color.rgb(25, 118, 210))
            setPadding(0, 0, 0, 24)
        }
        root.addView(title)

        // ĐỌC LỊCH KHÁM ĐÃ LƯU
        val bookings = getSharedPreferences(
            "MediCareAppointments",
            MODE_PRIVATE
        ).getString("bookings", "") ?: ""

        if (bookings.isBlank()) {
            val emptyMessage = TextView(this).apply {
                text = "Bạn chưa có lịch khám nào."
                textSize = 17f
                setTextColor(Color.DKGRAY)
            }
            root.addView(emptyMessage)
        } else {
            bookings.lines()
                .filter { it.isNotBlank() }
                .forEach { line ->

                    val parts = line.split("|")

                    if (parts.size == 4) {
                        val card = TextView(this).apply {
                            text = "Bác sĩ: ${parts[0]}\n" +
                                    "Chuyên khoa: ${parts[1]}\n" +
                                    "Ngày khám: ${parts[2]}\n" +
                                    "Giờ khám: ${parts[3]}\n" +
                                    "Trạng thái: Đã đặt"

                            textSize = 16f
                            setTextColor(Color.DKGRAY)
                            setPadding(20, 20, 20, 20)
                            setBackgroundColor(Color.WHITE)
                        }

                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        params.bottomMargin = 16

                        root.addView(card, params)
                    }
                }
        }

        val backButton = Button(this).apply {
            text = "Quay lại"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        root.addView(backButton)

        val scrollView = ScrollView(this)
        scrollView.addView(root)
        setContentView(scrollView)
    }
}
