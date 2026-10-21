
package ui.patient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.PaymentActivity
import com.example.medicare.R

class PatientHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_home)

        val btnPayment = findViewById<Button>(R.id.btnPayment)
        val btnNotifications = findViewById<Button>(R.id.btnNotifications)
        val btnConsultationChat = findViewById<Button>(R.id.btnConsultationChat)

        btnPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(
                this,
                "Chức năng thông báo sẽ được bổ sung sau",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnConsultationChat.setOnClickListener {
            Toast.makeText(
                this,
                "Chức năng chat tư vấn sẽ được bổ sung sau",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
