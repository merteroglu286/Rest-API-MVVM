package tr.main.elephantapps_sprint1.model.response.Home

data class Data(
    val cartCount: Int,
    val categories: List<Category>,
    val garage: Garage,
    val garages: List<Garages>,
    val lastAnnouncement: LastAnnouncement,
    val lastEvent: LastEvent,
    val lastVideo: LastVideo,
    val popularSearch: List<PopularSearch>,
    val products: List<Product>,
    val randomGlossary: RandomGlossary,
    val tomopuan: Int,
    val urgentProducts: List<UrgentProduct>,
    val userGarage: UserGarage
)