package model

data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val role: String = "PATIENT" // Mặc định là PATIENT. Các quyền khác: "DOCTOR", "ADMIN"
)