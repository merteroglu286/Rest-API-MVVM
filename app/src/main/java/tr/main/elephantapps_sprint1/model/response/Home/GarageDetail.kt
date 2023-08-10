package tr.main.elephantapps_sprint1.model.response.Home

data class GarageDetail(
    val bannerUrl: String,
    val followerCount: Int,
    val garageType: String,
    val id: Int,
    val logoUrl: String,
    val score: Int,
    val userName: String
)