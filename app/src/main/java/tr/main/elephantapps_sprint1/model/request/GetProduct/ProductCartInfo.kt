package tr.main.elephantapps_sprint1.model.request.GetProduct

data class ProductCartInfo(
    val productQuantityInCart: Int,
    val totalPrice: Int
)