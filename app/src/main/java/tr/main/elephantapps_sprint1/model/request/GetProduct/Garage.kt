package tr.main.elephantapps_sprint1.model.request.GetProduct

data class Garage(
    val bannerUrl: String,
    val followerCount: Int,
    val garageType: String,
    val id: Int,
    val isFollowed: Boolean,
    val logoUrl: String,
    val productCount: Int,
    val score: Int,
    val soldProducts: Int,
    val userName: String
)