package tr.main.elephantapps_sprint1.model.response.Comment

data class Data(
    val brandName: String,
    val comments: List<Comment>,
    val id: Int,
    val photoUrl: String,
    val score: Double,
    val title: String
)