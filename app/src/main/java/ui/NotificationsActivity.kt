
package ui.patient

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.medicare.R

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val btnBack = findViewById<Button>(R.id.btnBackToPatientHome)

        btnBack.setOnClickListener {
            finish()
        }
    }
}
