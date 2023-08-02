package tr.main.elephantapps_sprint1.model.request

data class UserModel(
    val fullName: String,
    val email: String,
    val password: String,
    val isOpenNotification: Boolean
)