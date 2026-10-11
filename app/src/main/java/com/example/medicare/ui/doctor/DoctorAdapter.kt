package com.example.medicare.ui.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicare.R
import com.example.medicare.model.Doctor

class DoctorAdapter(
    private var doctorList: List<Doctor>,
    private val onItemClick: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    private var fullList: List<Doctor> = doctorList

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvSpecialty: TextView = itemView.findViewById(R.id.tvSpecialty)
        val tvExperience: TextView = itemView.findViewById(R.id.tvExperience)
        val tvHospital: TextView = itemView.findViewById(R.id.tvHospital)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]

        // Gán dữ liệu hiển thị tên, chuyên khoa, kinh nghiệm
        holder.tvDoctorName.text = doctor.name
        holder.tvSpecialty.text = doctor.specialty
        holder.tvExperience.text = doctor.experience
        holder.tvHospital.text = doctor.hospital

        // Sự kiện khi bệnh nhân chọn/chạm vào bác sĩ
        holder.itemView.setOnClickListener {
            onItemClick(doctor)
        }
    }

    override fun getItemCount(): Int = doctorList.size

    // Cập nhật danh sách khi tải từ Repository về
    fun updateData(newList: List<Doctor>) {
        this.doctorList = newList
        this.fullList = newList
        notifyDataSetChanged()
    }

    // Xử lý lọc danh sách khi gõ ô tìm kiếm
    fun filter(query: String) {
        doctorList = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.specialty.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}