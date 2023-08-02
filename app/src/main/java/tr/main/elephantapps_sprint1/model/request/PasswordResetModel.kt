package tr.main.elephantapps_sprint1.model.request

data class PasswordResetModel(
    val email: String,
    val code : String,
    val newPassword: String
)
