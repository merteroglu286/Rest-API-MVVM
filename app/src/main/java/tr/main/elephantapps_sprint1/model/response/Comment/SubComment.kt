package tr.main.elephantapps_sprint1.model.response.Comment

data class SubComment(
    val commentDate: String,
    val commentType: String,
    val id: Int,
    val parentId: Int,
    val score: Int,
    val subComments: List<Any>,
    val text: String,
    val userName: String
)