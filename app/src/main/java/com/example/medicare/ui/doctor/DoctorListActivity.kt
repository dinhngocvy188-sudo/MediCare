package com.example.medicare.ui.doctor

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicare.R
import com.example.medicare.repository.AuthRepository
import com.example.medicare.repository.DoctorRepository
import com.example.medicare.ui.auth.LoginActivity

class DoctorListActivity : AppCompatActivity() {

    private val doctorRepository = DoctorRepository()
    private val authRepository = AuthRepository()
    private lateinit var adapter: DoctorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)

        val rvDoctors = findViewById<RecyclerView>(R.id.rvDoctors)
        val searchView = findViewById<SearchView>(R.id.searchViewDoctor)

        adapter = DoctorAdapter(emptyList()) { doctor ->
            val intent = Intent(this, DoctorDetailActivity::class.java)
            intent.putExtra("DOCTOR", doctor)
            startActivity(intent)
        }

        rvDoctors.layoutManager = LinearLayoutManager(this)
        rvDoctors.adapter = adapter

        // Tải danh sách bác sĩ từ Firebase
        doctorRepository.getDoctors(
            onSuccess = { doctorList ->
                adapter.updateData(doctorList)
            },
            onFailure = { error ->
                Toast.makeText(this, "Lỗi tải dữ liệu: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )

        // Xử lý tìm kiếm
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })
    }
}