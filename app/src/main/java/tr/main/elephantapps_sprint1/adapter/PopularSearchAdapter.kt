package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.CategoryAndBrandItemLayoutForSearchResultBinding
import tr.main.elephantapps_sprint1.databinding.CategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.databinding.PopulerSearchLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Home.PopularSearch

class PopularSearchAdapter(private val searches: ArrayList<PopularSearch>
) :
    RecyclerView.Adapter<PopularSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryAndBrandItemLayoutForSearchResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val search = searches[position]

        holder.tvName.text = search.title



    }

    override fun getItemCount(): Int {
        return searches.size
    }


    class ViewHolder(binding: CategoryAndBrandItemLayoutForSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.txtName
    }
}