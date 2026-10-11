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
                Toast.LENGTH_SHORT,
            ).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.tvDetailName).text = doctor.displayName

        findViewById<TextView>(R.id.tvDetailSpecialty).text = doctor.displaySpecialty

        findViewById<TextView>(R.id.tvDetailDescription).text =
            doctor.description.ifEmpty { getString(R.string.no_description) }

        findViewById<Button>(R.id.btnBookAppointment)
            .setOnClickListener {
                openBookingScreen(doctor)
            }
    }

    private fun getDoctorFromIntent(): Doctor? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(
                EXTRA_DOCTOR_DATA,
                Doctor::class.java,
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_DOCTOR_DATA) as? Doctor
        }
    }

    private fun openBookingScreen(doctor: Doctor) {
        try {
            val bookingClass = Class.forName(
                "com.example.medicare.ui.appointment.SelectDateActivity",
            )

            val bookingIntent = Intent(this, bookingClass).apply {
                putExtra("doctorId", doctor.displayId)
                putExtra("consultationFee", doctor.consultationFee)
            }

            startActivity(bookingIntent)
        } catch (_: ClassNotFoundException) {
            Toast.makeText(
                this,
                "Chức năng đặt lịch chưa được triển khai",
                Toast.LENGTH_SHORT,
            ).show()
        } catch (_: Exception) {
            Toast.makeText(
                this,
                "Không thể mở màn hình đặt lịch",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
