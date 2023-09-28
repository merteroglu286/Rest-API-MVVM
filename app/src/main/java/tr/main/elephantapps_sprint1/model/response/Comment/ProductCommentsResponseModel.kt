package tr.main.elephantapps_sprint1.model.response.Comment

data class ProductCommentsResponseModel(
    val `data`: Data,
    val message: String,
    val success: Boolean
)