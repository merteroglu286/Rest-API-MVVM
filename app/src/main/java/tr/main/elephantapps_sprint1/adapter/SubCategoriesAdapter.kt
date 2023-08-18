package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.SubcategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory

class SubCategoriesAdapter (private var subCategories: ArrayList<SubCategory>, private val onItemClick: (SubCategory) -> Unit
) :
    RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubcategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val category = subCategories[position]
        holder.tvName.text = category.name

        holder.itemView.setOnClickListener{
            onItemClick(category)
        }

    }


    override fun getItemCount(): Int {
        return subCategories.size

    }

    class ViewHolder(binding: SubcategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameCategory
    }

    fun setFilteredList(mList: ArrayList<SubCategory>){
        this.subCategories = mList
        notifyDataSetChanged()
    }
}
