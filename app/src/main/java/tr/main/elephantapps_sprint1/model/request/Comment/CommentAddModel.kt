package tr.main.elephantapps_sprint1.model.request.Comment

data class CommentAddModel(
    val commentType: String,
    val parentId: Int,
    val productId: Int,
    val score: Int,
    val text: String
)