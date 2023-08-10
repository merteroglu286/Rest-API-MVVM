package tr.main.elephantapps_sprint1.model.request

data class HomeSearchRequestModel(
    val approvalStatus: String,
    val pagingParameters: PagingParameters,
    val saleStatus: String,
    val searchText: String
)