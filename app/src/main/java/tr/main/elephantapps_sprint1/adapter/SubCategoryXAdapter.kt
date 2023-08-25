package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.SubcategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Category.SubCategoryX

class SubCategoryXAdapter (private var categories: ArrayList<SubCategoryX>, private val onItemClick: (SubCategoryX) -> Unit
) :
    RecyclerView.Adapter<SubCategoryXAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubcategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val category = categories[position]
        holder.tvName.text = category.name

        holder.itemView.setOnClickListener{
            onItemClick(category)
        }

    }


    override fun getItemCount(): Int {
        return categories.size

    }

    class ViewHolder(binding: SubcategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameCategory
    }

    fun setFilteredList(mList: ArrayList<SubCategoryX>){
        this.categories = mList
        notifyDataSetChanged()
    }
}