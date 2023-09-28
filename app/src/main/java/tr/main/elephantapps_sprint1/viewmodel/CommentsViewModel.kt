package tr.main.elephantapps_sprint1.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.Comment.CommentAddModel
import tr.main.elephantapps_sprint1.model.response.Comment.Comment
import tr.main.elephantapps_sprint1.model.response.Comment.Data
import tr.main.elephantapps_sprint1.model.response.Comment.ProductCommentsResponseModel
import tr.main.elephantapps_sprint1.model.response.Comment.SubComment
import tr.main.elephantapps_sprint1.repository.AppRepo

class CommentsViewModel:ViewModel() {

    val data = MutableLiveData<Data>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    val comments = MutableLiveData<List<Comment>>()
    val subComment = MutableLiveData<List<SubComment>>()

    fun postCommentToApi(commentAddModel: CommentAddModel) {
        AppRepo.callApiCommentPost(commentAddModel){success, message ->
            successLiveData.value = success
            errorLiveData.value = message
        }
    }


    fun getDataFromAPI(id: Int) {
        AppRepo.callApiGetCommentByProductId(id){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message

            if (successLiveData.value == true){
                this.data.value = data?.data
                this.comments.value = data?.data?.comments
/*
                val subCommentsList = mutableListOf<SubComment>()
                data?.data?.comments?.forEach { comment ->
                    subCommentsList.addAll(comment.subComments)
                }
                this.subComment.value = subCommentsList

 */
            }
        }
    }
}