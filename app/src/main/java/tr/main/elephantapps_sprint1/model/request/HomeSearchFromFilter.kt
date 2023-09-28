package tr.main.elephantapps_sprint1.model.request

data class HomeSearchFromFilter(
    val approvalStatus: String? = null,
    var brandId: Int? = 0,
    var categoryId: Int? = 0,
    var maxPrice: Int? = 0,
    var minPrice: Int? = 0,
    val pagingParameters: PagingParameters? = null,
    var productStatus: String? = null,
    val saleStatus: String? = null
)