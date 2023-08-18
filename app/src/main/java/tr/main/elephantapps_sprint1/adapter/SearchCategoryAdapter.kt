package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.SubcategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.enums.SearchCategoryType
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.model.response.Home.Category
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import java.util.Locale

class SearchCategoryAdapter (private val categories: ArrayList<Data>?, private val onItemClick: (Data) -> Unit
) :
    RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubcategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val category = categories!![position]
        holder.tvName.text = category.name

        holder.itemView.setOnClickListener {
            onItemClick(category)
        }

    }

    override fun getItemCount(): Int {
        return categories!!.size

    }


    class ViewHolder(binding: SubcategoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameCategory
    }

}

