package tr.main.elephantapps_sprint1.model.request

data class ProductRequestModel(
    val brandId: Int,
    val categoryId: Int,
    val description: String,
    val name: String
)