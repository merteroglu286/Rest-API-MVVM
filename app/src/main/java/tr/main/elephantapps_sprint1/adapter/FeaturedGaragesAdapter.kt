package tr.main.elephantapps_sprint1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.databinding.FeaturedGaragesItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Home.Garages
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.GarageProfile

class FeaturedGaragesAdapter(private val garages: ArrayList<Garages>,private val activity: Activity
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

        if (garage.logoUrl.isNullOrEmpty()){
            Glide.with(context).load(R.drawable.baseline_person_outline_24).into(holder.image)

            val paddingInDp = 8
            val density = context.resources.displayMetrics.density
            val paddingInPixels = (paddingInDp * density).toInt()
            holder.image.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

        }else{
            Glide.with(context).load(Constans.GARAGE_LOGOS_URL+garage.logoUrl).into(holder.image)
        }



    }

    override fun getItemCount(): Int {
        return 5
    }


    class ViewHolder(binding: FeaturedGaragesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.txtNameGarage
        val image = binding.ivGarage
    }
}