package tr.main.elephantapps_sprint1.model.response.Search

data class Brand(
    val approvalStatus: String,
    val categoryId: Int,
    val categoryName: String,
    val id: Int,
    val isDeleted: Boolean,
    val name: String
)