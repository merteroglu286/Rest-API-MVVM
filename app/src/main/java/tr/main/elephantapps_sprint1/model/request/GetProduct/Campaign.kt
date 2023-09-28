package tr.main.elephantapps_sprint1.model.request.GetProduct

data class Campaign(
    val discountRate: Double,
    val discountedPrice: Double,
    val normalPrice: Double
)