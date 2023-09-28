package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.ItemLayoutSubCommentBinding
import tr.main.elephantapps_sprint1.model.response.Comment.SubComment

class SubCommentAdapter (private val subComments: List<SubComment>
) :
    RecyclerView.Adapter<SubCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutSubCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val subComment = subComments[position]

        holder.tvName.text = subComment.userName
        holder.tvDate.text = subComment.commentDate
        holder.tvComment.text = subComment.text

    }

    override fun getItemCount(): Int {
        return subComments.size
    }


    class ViewHolder(binding: ItemLayoutSubCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvCommentUserName
        val tvDate = binding.tvDate
        val tvComment = binding.tvComment
    }
}