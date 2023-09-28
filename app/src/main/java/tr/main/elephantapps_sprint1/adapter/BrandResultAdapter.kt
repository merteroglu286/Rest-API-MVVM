package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.CategoryAndBrandItemLayoutForSearchResultBinding
import tr.main.elephantapps_sprint1.model.response.Brand.Data
import tr.main.elephantapps_sprint1.model.response.Search.Brand
import java.util.Locale

class BrandResultAdapter(private val brands: ArrayList<Brand>
) :
    RecyclerView.Adapter<BrandResultAdapter.ViewHolder>() {

    private var allBrands: ArrayList<Brand> = brands

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryAndBrandItemLayoutForSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val brand = brands[position]

        holder.tvName.text = brand.name
        holder.tvTitle.text = "Marka"


    }

    override fun getItemCount(): Int {
        return brands.size
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
                        allBrands = brands
                    else {
                        val filterContact = ArrayList<Brand>()
                        for (brand in brands) {
                            if (brand.name.toLowerCase(Locale.ROOT).trim()
                                    .contains(searchContent.toLowerCase(Locale.ROOT).trim())
                            )
                                filterContact.add(brand)
                        }
                        allBrands = filterContact
                    }
                    val filterResults = FilterResults()
                    filterResults.values = allBrands
                    return filterResults
                }
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    allBrands = results?.values as ArrayList<Brand>
                    notifyDataSetChanged()
                }
            }
        }
     */
}
