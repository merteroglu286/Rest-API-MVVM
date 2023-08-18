package tr.main.elephantapps_sprint1.model.response.Brand

data class BrandModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)