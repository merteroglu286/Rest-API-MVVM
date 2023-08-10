package tr.main.elephantapps_sprint1.model.response.Home

data class Product(
    val approve: String,
    val brandName: String,
    val campaign: Campaign,
    val campaignRate: Int,
    val endDate: String,
    val garageId: Int,
    val id: Int,
    val photoUrl: String,
    val price: Int,
    val startDate: String,
    val stock: Int,
    val title: String
)