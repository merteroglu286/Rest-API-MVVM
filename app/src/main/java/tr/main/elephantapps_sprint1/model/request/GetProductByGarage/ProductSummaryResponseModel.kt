package tr.main.elephantapps_sprint1.model.request.GetProductByGarage

data class ProductSummaryResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)