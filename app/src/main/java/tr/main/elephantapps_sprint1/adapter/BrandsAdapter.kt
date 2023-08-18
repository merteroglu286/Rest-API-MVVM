package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.BrandItemLayoutBinding
import tr.main.elephantapps_sprint1.databinding.SubcategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Brand.Data

class BrandsAdapter(private var brandList: ArrayList<Data>, private val onItemClick: (Data) -> Unit
) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BrandItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val brand = brandList[position]
        holder.tvName.text = brand.name

        holder.itemView.setOnClickListener{
            onItemClick(brand)
        }

    }


    override fun getItemCount(): Int {
        return brandList.size

    }

    class ViewHolder(binding: BrandItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameBrand
    }

    fun setFilteredList(mList: ArrayList<Data>){
        this.brandList = mList
        notifyDataSetChanged()
    }
}
