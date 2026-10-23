
package ui.patient

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.text.Editable
import android.text.TextWatcher
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class PatientHomeActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var doctorContainer: LinearLayout

    private val specialties = listOf(
        "Tất cả",
        "Tim mạch",
        "Nhi khoa",
        "Da liễu",
        "Thần kinh",
        "Nội tổng quát"
    )

    private val doctors = listOf(
        Doctor("BS. Nguyễn Văn An", "Tim mạch"),
        Doctor("BS. Trần Thu Hà", "Nhi khoa"),
        Doctor("BS. Lê Hoàng Nam", "Da liễu"),
        Doctor("BS. Phạm Ngọc Mai", "Thần kinh"),
        Doctor("BS. Võ Thanh Bình", "Nội tổng quát")
    )

    private var selectedSpecialty = "Tất cả"

    data class Doctor(
        val name: String,
        val specialty: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createPatientInterface()
        showDoctors(doctors)
    }

    private fun createPatientInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(245, 248, 252))
        }

        // THANH TIÊU ĐỀ
        val header = TextView(this).apply {
            text = "♥  MediCare"
            textSize = 26f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(25, 118, 210))
            setPadding(20, 24, 20, 24)
            gravity = Gravity.CENTER_VERTICAL
        }
        root.addView(header)

        // NÚT ĐĂNG XUẤT
        val logoutButton = Button(this).apply {
            text = "Đăng xuất"
            isAllCaps = false
            textSize = 14f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(220, 53, 69))

            setOnClickListener {
                val intent = Intent(
                    this@PatientHomeActivity,
                    ui.auth.LoginActivity::class.java
                )

                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }
        }

        root.addView(
            logoutButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 8, 16, 0)
            }
        )

        val scrollView = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 20, 20, 20)
        }

        scrollView.addView(content)

        val welcome = TextView(this).apply {
            text = "Xin chào! Bạn cần tìm bác sĩ?"
            textSize = 21f
            setTextColor(Color.rgb(35, 50, 70))
            setPadding(0, 0, 0, 16)
        }
        content.addView(welcome)

        searchInput = EditText(this).apply {
            hint = "Tìm tên bác sĩ hoặc chuyên khoa..."
            textSize = 15f
            maxLines = 1
            setSingleLine(true)
            setPadding(16, 12, 16, 12)
            setBackgroundColor(Color.WHITE)
        }

        content.addView(
            searchInput,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 24
            }
        )

        content.addView(makeTitle("Chuyên khoa"))

        val specialtyScroll = HorizontalScrollView(this)
        val specialtyRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        specialties.forEach { specialty ->
            val button = Button(this).apply {
                text = specialty
                isAllCaps = false
                textSize = 12f

                setOnClickListener {
                    selectedSpecialty = specialty
                    filterDoctors()
                }
            }

            specialtyRow.addView(
                button,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 8
                }
            )
        }

        specialtyScroll.addView(specialtyRow)

        content.addView(
            specialtyScroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 24
            }
        )

        content.addView(makeTitle("Danh sách bác sĩ"))

        doctorContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        content.addView(doctorContainer)

        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                filterDoctors()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun makeTitle(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 19f
            setTextColor(Color.rgb(35, 50, 70))
            setPadding(0, 0, 0, 12)
        }
    }

    private fun filterDoctors() {
        val keyword = searchInput.text.toString().trim()

        val filtered = doctors.filter { doctor ->
            val matchesKeyword =
                doctor.name.contains(keyword, ignoreCase = true) ||
                        doctor.specialty.contains(keyword, ignoreCase = true)

            val matchesSpecialty =
                selectedSpecialty == "Tất cả" ||
                        doctor.specialty == selectedSpecialty

            matchesKeyword && matchesSpecialty
        }

        showDoctors(filtered)
    }

    private fun showDoctors(list: List<Doctor>) {
        doctorContainer.removeAllViews()

        if (list.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "Không tìm thấy bác sĩ phù hợp."
                textSize = 15f
                setTextColor(Color.GRAY)
                setPadding(0, 16, 0, 16)
            }

            doctorContainer.addView(emptyText)
            return
        }

        list.forEach { doctor ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(18, 16, 18, 16)
                setBackgroundColor(Color.WHITE)
                isClickable = true
                isFocusable = true

                setOnClickListener {
                    val intent = Intent(
                        this@PatientHomeActivity,
                        BookingActivity::class.java
                    )

                    intent.putExtra("doctorName", doctor.name)
                    intent.putExtra("specialty", doctor.specialty)

                    startActivity(intent)
                }
            }

            val name = TextView(this).apply {
                text = doctor.name
                textSize = 17f
                setTextColor(Color.rgb(25, 118, 210))
            }

            val specialtyText = TextView(this).apply {
                text = "Chuyên khoa: ${doctor.specialty}"
                textSize = 14f
                setTextColor(Color.DKGRAY)
                setPadding(0, 8, 0, 0)
            }

            val hint = TextView(this).apply {
                text = "Nhấn để đặt lịch khám  →"
                textSize = 13f
                setTextColor(Color.rgb(25, 118, 210))
                setPadding(0, 12, 0, 0)
            }

            card.addView(name)
            card.addView(specialtyText)
            card.addView(hint)

            doctorContainer.addView(
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
}
