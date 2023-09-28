package tr.main.elephantapps_sprint1.model.request

data class HomeSearchRequestModel(
    val pagingParameters: PagingParameters,
    val searchText: String
)