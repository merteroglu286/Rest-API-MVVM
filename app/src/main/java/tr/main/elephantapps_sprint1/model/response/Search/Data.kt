package tr.main.elephantapps_sprint1.model.response.Search

data class Data(
    val brands: List<Brand>?,
    val categories: List<Category>?,
    val garages: List<Garage>?,
    val pageSize: Int,
    val products: List<Product>?
)