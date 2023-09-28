package tr.main.elephantapps_sprint1.model.request.GetProduct

data class GarageProduct(
    val approvalStatus: String,
    val brandName: String,
    val campaign: Campaign,
    val description: String,
    val garageId: Int,
    val id: Int,
    val photoUrl: String,
    val price: Double,
    val saleStatus: String,
    val title: String
)