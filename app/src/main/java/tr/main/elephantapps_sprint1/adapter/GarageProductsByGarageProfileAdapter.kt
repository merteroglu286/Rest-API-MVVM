package tr.main.elephantapps_sprint1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.ProductDetail
import tr.main.elephantapps_sprint1.databinding.FeaturedProductsItemLayoutBinding
import tr.main.elephantapps_sprint1.model.request.GetProductByGarage.Data

class GarageProductsByGarageProfileAdapter(private val products: List<Data>, private val activity: Activity
) :
    RecyclerView.Adapter<GarageProductsByGarageProfileAdapter.ViewHolder>() {

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

        holder.itemView.setOnClickListener{
            val intent = Intent(activity, ProductDetail::class.java)
            intent.putExtra("id",product.id)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

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

}