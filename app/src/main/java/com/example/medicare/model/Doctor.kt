package com.example.medicare.model

import java.io.Serializable

data class Doctor(
    val id: String = "",
    val doctorId: String = "",
    val userId: String = "",
    val name: String = "",
    val fullName: String = "",
    val specialty: String = "",
    val specialtyId: String = "",
    val experience: String = "",
    val hospital: String = "",
    val phone: String = "",
    val description: String = "",
    val consultationFee: Long = 0,
    val active: Boolean = true,
    val imageUrl: String = "",
    val rating: Double = 5.0,
) : Serializable {
    val displayId: String
        get() = id.ifEmpty { doctorId }

    val displayName: String
        get() = name.ifEmpty { fullName }

    val displaySpecialty: String
        get() = specialty.ifEmpty { specialtyId }
}
