package tr.main.elephantapps_sprint1.model.response.Home

data class LastVideo(
    val clickUrlCount: Int,
    val date: String,
    val id: Int,
    val isPublished: Boolean,
    val showPopup: Boolean,
    val showPopupCount: Int,
    val title: String,
    val videoUrl: String
)