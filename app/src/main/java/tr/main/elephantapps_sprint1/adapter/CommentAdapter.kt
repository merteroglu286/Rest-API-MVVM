package tr.main.elephantapps_sprint1.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.DialogAddCommentBinding
import tr.main.elephantapps_sprint1.databinding.ItemLayoutCommentBinding
import tr.main.elephantapps_sprint1.model.response.Comment.Comment

class CommentAdapter (private val comments: List<Comment>,private val onItemClick: (Comment,String) -> Unit
) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val comment = comments[position]

        holder.tvName.text = comment.userName
        holder.tvDate.text = comment.commentDate
        holder.tvComment.text = comment.text

        val starImageViews = arrayOf(
            holder.star1,
            holder.star2,
            holder.star3,
            holder.star4,
            holder.star5
        )

        for (i in 0 until 5) {
            val starImageView = starImageViews[i]
            val isFilled = i < comment.score
            val drawableRes = if (isFilled) R.drawable.baseline_star_24 else R.drawable.baseline_star_outline_24
            starImageView.setImageResource(drawableRes)
        }

        if (!comment.subComments.isEmpty()){
            val commentAdapter = SubCommentAdapter(comment.subComments)
            holder.rvSubComment.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false)
            holder.rvSubComment.adapter = commentAdapter
        }

        holder.tvAnswer.setOnClickListener {
            val dialogBinding = DialogAddCommentBinding.inflate(LayoutInflater.from(it.context))
            val alertDialog = AlertDialog.Builder(it.context)
            alertDialog.setView(dialogBinding.root)
            val dialog = alertDialog.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.window?.attributes?.windowAnimations = R.style.SlideUpDialogAnimation
            dialog.show()

            dialogBinding.tvComment.text = comment.text
            dialogBinding.tvDate.text = comment.commentDate
            dialogBinding.tvCommentUserName.text = comment.userName

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogBinding.btnSend.setOnClickListener {
                onItemClick(comment,dialogBinding.etAddComment.text.toString())
                dialog.dismiss()
                notifyDataSetChanged()
            }
            dialogBinding.tvCancel.setOnClickListener {
                dialog.dismiss()
            }
        }



    }

    override fun getItemCount(): Int {
        return comments.size
    }


    class ViewHolder(binding: ItemLayoutCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvCommentUserName
        val tvDate = binding.tvDate
        val tvComment = binding.tvComment
        val tvAnswer = binding.tvAnswer
        val rvSubComment = binding.rvSubComment

        val star1 = binding.star1
        val star2 = binding.star2
        val star3 = binding.star3
        val star4 = binding.star4
        val star5 = binding.star5
    }
}