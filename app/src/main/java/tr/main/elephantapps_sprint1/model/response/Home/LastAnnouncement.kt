package tr.main.elephantapps_sprint1.model.response.Home

data class LastAnnouncement(
    val buttonName: String,
    val buttonUrl: String,
    val clickUrlCount: Int,
    val date: String,
    val description: String,
    val hasButton: Boolean,
    val id: Int,
    val isPublished: Boolean,
    val name: String,
    val photoUrl: String,
    val showPopup: Boolean,
    val showPopupCount: Int
)