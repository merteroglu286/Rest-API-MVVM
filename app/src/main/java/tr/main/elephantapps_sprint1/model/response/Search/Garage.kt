package tr.main.elephantapps_sprint1.model.response.Search

data class Garage(
    val bannerUrl: String,
    val followerCount: Int,
    val garageType: String,
    val id: Int,
    val logoUrl: String,
    val score: Int,
    val userName: String
)