package tr.main.elephantapps_sprint1.model.response.Category

data class Data(
    val iconUrl: String,
    val id: Int,
    val name: String,
    val parent: Any,
    val parentId: Any,
    val subCategories: List<SubCategory>
)