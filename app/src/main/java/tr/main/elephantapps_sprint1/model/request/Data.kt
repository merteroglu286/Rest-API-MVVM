package tr.main.elephantapps_sprint1.model.request

data class Data(
    val accessToken: String,
    val expiration: String,
    val fullName: String,
    val isSocialMediaAccount: Boolean,
    val refreshToken: String,
    val userId: Int
)