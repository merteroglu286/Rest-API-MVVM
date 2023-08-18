package tr.main.elephantapps_sprint1.model.response.Category

data class CategoriesResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)