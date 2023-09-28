package tr.main.elephantapps_sprint1.model.request.GetProduct

data class ProductDetailSummaryResponseModel(
    val `data`: Data,
    val message: String,
    val success: Boolean
)