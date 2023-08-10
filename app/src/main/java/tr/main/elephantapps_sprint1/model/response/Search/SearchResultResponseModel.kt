package tr.main.elephantapps_sprint1.model.response.Search

data class SearchResultResponseModel(
    val `data`: Data?,
    val message: String,
    val success: Boolean
)