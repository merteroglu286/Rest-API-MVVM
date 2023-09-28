package tr.main.elephantapps_sprint1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.FilterProduct
import tr.main.elephantapps_sprint1.databinding.CategoryAndBrandItemLayoutForSearchResultBinding
import tr.main.elephantapps_sprint1.model.response.Search.Category
import java.util.Locale

class CategoryResultAdapter(private val categories: ArrayList<Category>
) :
    RecyclerView.Adapter<CategoryResultAdapter.ViewHolder>(){

    private var allCategories: ArrayList<Category> = categories

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryAndBrandItemLayoutForSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val category = categories[position]

        holder.tvName.text = category.name
        holder.tvTitle.text = "Kategori"


    }

    override fun getItemCount(): Int {
        return categories.size
    }


    class ViewHolder(binding: CategoryAndBrandItemLayoutForSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.txtName
        val tvTitle = binding.txtTitle
    }
    /*
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val searchContent = constraint.toString()
                    if (searchContent.isEmpty())
                        allCategories = categories
                    else {
                        val filterContact = ArrayList<Category>()
                        for (Category in categories) {
                            if (Category.name.toLowerCase(Locale.ROOT).trim()
                                    .contains(searchContent.toLowerCase(Locale.ROOT).trim())
                            )
                                filterContact.add(Category)
                        }
                        allCategories = filterContact
                    }
                    val filterResults = FilterResults()
                    filterResults.values = allCategories
                    return filterResults
                }
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    allCategories = results?.values as ArrayList<Category>
                    notifyDataSetChanged()
                }
            }
        }
     */
}


