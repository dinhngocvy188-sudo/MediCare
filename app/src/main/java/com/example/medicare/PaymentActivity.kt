
package com.example.medicare

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    private lateinit var btnConfirmPayment: Button
    private lateinit var txtPaymentStatus: TextView

    private var isPaid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
        txtPaymentStatus = findViewById(R.id.txtPaymentStatus)

        btnConfirmPayment.setOnClickListener {
            if (!isPaid) {
                isPaid = true

                txtPaymentStatus.text =
                    "Trạng thái: Thanh toán mô phỏng thành công"

                txtPaymentStatus.setTextColor(
                    android.graphics.Color.rgb(46, 125, 50)
                )

                txtPaymentStatus.setBackgroundColor(
                    android.graphics.Color.rgb(225, 245, 227)
                )

                btnConfirmPayment.text = "Đã xác nhận thanh toán"
                btnConfirmPayment.isEnabled = false

                Toast.makeText(
                    this,
                    "Thanh toán mô phỏng thành công!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
