package tr.main.elephantapps_sprint1.model.response.Home

data class LastEvent(
    val buttonName: String,
    val buttonUrl: String,
    val date: String,
    val description: String,
    val endDate: String,
    val hasButton: Boolean,
    val id: Int,
    val isPublished: Boolean,
    val lat: Double,
    val lng: Double,
    val name: String,
    val photoUrl: String,
    val showPopup: Boolean,
    val startDate: String
)