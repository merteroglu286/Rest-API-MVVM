package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.databinding.GarageItemLayoutForSearchResultBinding
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import tr.main.elephantapps_sprint1.util.Constans
import java.util.Locale

class GarageResultAdapter(private val garages: ArrayList<Garage>
) :
    RecyclerView.Adapter<GarageResultAdapter.ViewHolder>() {

    private var allGarages: ArrayList<Garage> = garages

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GarageItemLayoutForSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val garage = garages[position]

        holder.tvName.text = garage.userName
        holder.tvTitle.text = "Garaj"

        Glide.with(context).load(Constans.GARAGE_LOGOS_URL+garage.logoUrl).into(holder.image)
    }

    override fun getItemCount(): Int {
        return garages.size
    }


    class ViewHolder(binding: GarageItemLayoutForSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.txtName
        val tvTitle = binding.txtTitle
        val image = binding.ivGarageItem
    }

/*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchContent = constraint.toString()
                if (searchContent.isEmpty())
                    allGarages = garages
                else {

                    val filterContact = ArrayList<Garage>()
                    for (garage in garages) {

                        if (garage.userName.toLowerCase(Locale.ROOT).trim()
                                .contains(searchContent.toLowerCase(Locale.ROOT).trim())
                        )
                            filterContact.add(garage)
                    }
                    allGarages = filterContact
                }

                val filterResults = FilterResults()
                filterResults.values = allGarages
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                allGarages = results?.values as ArrayList<Garage>
                notifyDataSetChanged()

            }
        }
    }

 */

}
