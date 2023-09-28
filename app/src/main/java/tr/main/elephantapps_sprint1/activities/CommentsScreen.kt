package tr.main.elephantapps_sprint1.activities

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.CommentAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityCommentsScreenBinding
import tr.main.elephantapps_sprint1.model.request.Comment.CommentAddModel
import tr.main.elephantapps_sprint1.model.request.GetProduct.Comment
import tr.main.elephantapps_sprint1.viewmodel.CommentsViewModel
import kotlin.collections.ArrayList

class CommentsScreen : BaseActivity() {

    private lateinit var binding : ActivityCommentsScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedCommentList = intent.getParcelableArrayListExtra<Comment>("commentList")
        val productName = intent.getStringExtra("productName")
        val productPhoto = intent.getStringExtra("productPhoto")
        val productId = intent.getIntExtra("productId",0)
        setValues(productName!!,productPhoto!!,receivedCommentList!!)

        getComments(productId)
    }

    private fun setValues(productName:String,productPhoto:String,receivedCommentList:ArrayList<Comment>){
        Glide.with(this@CommentsScreen).load(Constans.PRODUCT_PHOTOS_URL+productPhoto).into(binding.ivProduct)
        binding.tvProductName.text = productName
        binding.tvCommentCount.text = receivedCommentList.size.toString()
        var score = 0.0
        for(comment in receivedCommentList){
            score += comment.score
        }
        score /= receivedCommentList.size

        val formattedScore = String.format("%.1f", score)
        binding.tvProductScore.text = formattedScore
        if (score> 3) {
            binding.tvProductScore.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {

            binding.tvProductScore.setTextColor(ContextCompat.getColor(this, R.color.primary))
        }

        val roundedScore = Math.round(score).toInt()

        val starImageViews = arrayOf(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5
        )

        for (i in 0 until 5) {
            val starImageView = starImageViews[i]
            val isFilled = i < roundedScore
            val drawableRes = if (isFilled) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24
            starImageView.setImageResource(drawableRes)
        }
    }


    private fun getComments(productId:Int){
        val viewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        viewModel.getDataFromAPI(productId)

        viewModel.successLiveData.observe(this, Observer{ success->
            println(success)

        })

        viewModel.errorLiveData.observe(this, Observer{ message->
            if (message != ""){
                Toast.makeText(this@CommentsScreen,message, Toast.LENGTH_LONG).show()
                println(message)
            }

        })

        viewModel.comments.observe(this, Observer { comments->

            val commentAdapter = CommentAdapter(comments) { comment, text ->

                val commentAddModel = CommentAddModel(
                    comment.commentType,
                    comment.id,
                    productId,
                    comment.score,
                    text
                )
                viewModel.postCommentToApi(commentAddModel)

                viewModel.successLiveData.observe(this, Observer{ success->
                    if (success == true){
                        Toast.makeText(this@CommentsScreen,"Başarılı", Toast.LENGTH_LONG).show()
                    }

                })
            }
            binding.rvComment.layoutManager = LinearLayoutManager(this@CommentsScreen,LinearLayoutManager.VERTICAL,false)
            binding.rvComment.adapter = commentAdapter
        })
    }
}