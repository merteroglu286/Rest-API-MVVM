package tr.main.elephantapps_sprint1.model.response.Category

data class SubCategory(
    val iconUrl: Any,
    val id: Int,
    val name: String,
    val parent: Any,
    val parentId: Int,
    val subCategories: List<SubCategoryX>
)