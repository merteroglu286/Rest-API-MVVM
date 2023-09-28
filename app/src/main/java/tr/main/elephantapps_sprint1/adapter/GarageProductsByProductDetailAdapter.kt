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
import tr.main.elephantapps_sprint1.model.request.GetProduct.GarageProduct

class GarageProductsByProductDetailAdapter(private val products: List<GarageProduct>, private val activity: Activity
) :
    RecyclerView.Adapter<GarageProductsByProductDetailAdapter.ViewHolder>() {

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
        if (products.size >=3){
            return 3
        }else{
            return products.size
        }

    }


    class ViewHolder(binding: FeaturedProductsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.txtTitle
        val tvBrandName = binding.txtBrandName
        val tvPrice = binding.txtPrice
        val image = binding.ivProduct
    }

}