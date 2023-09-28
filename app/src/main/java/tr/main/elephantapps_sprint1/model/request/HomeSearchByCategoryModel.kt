package tr.main.elephantapps_sprint1.model.request

data class HomeSearchByCategoryModel (
    val approvalStatus: String,
    val pagingParameters: PagingParameters,
    val saleStatus: String,
    val categoryId: Int
)