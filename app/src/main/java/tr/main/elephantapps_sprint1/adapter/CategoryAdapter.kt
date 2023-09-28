package tr.main.elephantapps_sprint1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.FilterProduct
import tr.main.elephantapps_sprint1.databinding.CategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Home.Category

class CategoryAdapter(private val categories: ArrayList<Category>,private val activity:Activity
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val category = categories[position]

        holder.tvName.text = category.name

        when (position) {
            0 -> holder.image.setImageResource(R.drawable.part)
            1 -> holder.image.setImageResource(R.drawable.wheel)
            2 -> holder.image.setImageResource(R.drawable.oil)
            3 -> holder.image.setImageResource(R.drawable.alet)
            4 -> holder.image.setImageResource(R.drawable.tshirt)
            5 -> holder.image.setImageResource(R.drawable.music)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context,FilterProduct::class.java)
            intent.putExtra("categoryId",category.id)
            context.startActivity(intent)
            activity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }


    }

    override fun getItemCount(): Int {
        return categories.size
    }


    class ViewHolder(binding: CategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameCategory
        val image = binding.ivCategory
    }
}