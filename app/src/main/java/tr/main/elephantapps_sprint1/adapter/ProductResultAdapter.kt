package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.databinding.FeaturedProductsItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.Constants.Constans

class ProductResultAdapter (private val products: ArrayList<Product>
) :
    RecyclerView.Adapter<ProductResultAdapter.ViewHolder>(){

    private var allProducts: ArrayList<Product> = products

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FeaturedProductsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val product = products[position]

        holder.tvTitle.text = product.title
        holder.tvBrandName.text = product.brandName
        holder.tvPrice.text = product.price.toString()

        Glide.with(context).load(Constans.PRODUCT_PHOTOS_URL+product.photoUrl).into(holder.image)


    }

    override fun getItemCount(): Int {
        return products.size
    }


    class ViewHolder(binding: FeaturedProductsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.txtTitle
        val tvBrandName = binding.txtBrandName
        val tvPrice = binding.txtPrice
        val image = binding.ivProduct
    }

/*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchContent = constraint.toString()
                if (searchContent.isEmpty())
                    allProducts = products
                else {

                    val filterContact = ArrayList<Product>()
                    for (product in products) {

                        if (product.brandName.toLowerCase(Locale.ROOT).trim()
                                .contains(searchContent.toLowerCase(Locale.ROOT).trim())
                        )
                            filterContact.add(product)
                    }
                    allProducts = filterContact
                }

                val filterResults = FilterResults()
                filterResults.values = allProducts
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                allProducts = results?.values as ArrayList<Product>
                notifyDataSetChanged()

            }
        }
    }

 */
}