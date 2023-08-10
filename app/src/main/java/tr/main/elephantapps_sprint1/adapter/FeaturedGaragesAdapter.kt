package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.CategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.databinding.FeaturedGaragesItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Home.Garages
import tr.main.elephantapps_sprint1.util.Constans

class FeaturedGaragesAdapter(private val garages: ArrayList<Garages>
) :
    RecyclerView.Adapter<FeaturedGaragesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FeaturedGaragesItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val garage = garages[position]

        holder.tvName.text = garage.userName

        Glide.with(context).load(Constans.GARAGE_LOGOS_URL+garage.logoUrl).into(holder.image)

    }

    override fun getItemCount(): Int {
        return 5
    }


    class ViewHolder(binding: FeaturedGaragesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.txtNameGarage
        val image = binding.ivGarage
    }
}