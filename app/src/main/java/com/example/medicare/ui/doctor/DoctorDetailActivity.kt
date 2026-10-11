
package com.example.medicare.ui.doctor

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R
import com.example.medicare.model.Doctor
import com.example.medicare.ui.appointment.SelectDateActivity

class DoctorDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DOCTOR_DATA = "DOCTOR_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)

        val doctor = getDoctorFromIntent()

        if (doctor == null) {
            Toast.makeText(
                this,
                "Không tìm thấy thông tin bác sĩ",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvDetailName).text =
            doctor.displayName

        findViewById<TextView>(R.id.tvDetailSpecialty).text =
            doctor.displaySpecialty

        findViewById<TextView>(R.id.tvDetailDescription).text =
            doctor.description.ifEmpty {
                getString(R.string.no_description)
            }

        findViewById<Button>(R.id.btnBookAppointment)
            .setOnClickListener {
                openBookingScreen(doctor)
            }
    }

    private fun getDoctorFromIntent(): Doctor? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(
                EXTRA_DOCTOR_DATA,
                Doctor::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_DOCTOR_DATA) as? Doctor
        }
    }

    private fun openBookingScreen(doctor: Doctor) {
        val doctorId = doctor.displayId

        if (doctorId.isBlank()) {
            Toast.makeText(
                this,
                "Mã bác sĩ không hợp lệ. Vui lòng thử lại.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val bookingIntent = Intent(
            this,
            SelectDateActivity::class.java
        ).apply {
            putExtra("doctorId", doctorId)
            putExtra("consultationFee", doctor.consultationFee)
        }

        startActivity(bookingIntent)
    }
}
