package tr.main.elephantapps_sprint1.model.response.Home

data class Category(
    val iconUrl: String,
    val id: Int,
    val name: String,
    val parent: String,
    val parentId: Int,
    val subCategories: List<Any>
)