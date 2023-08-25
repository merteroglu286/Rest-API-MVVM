package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tr.main.elephantapps_sprint1.databinding.SubcategoryItemLayoutBinding
import tr.main.elephantapps_sprint1.model.response.Category.Data

class SearchCategoryAllAdapter(
    private var nameList: ArrayList<String>,
    private var onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchCategoryAllAdapter.ViewHolder>() {

    private var filteredNames: ArrayList<String> = ArrayList()

    init {
        filteredNames.addAll(nameList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SubcategoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val name = filteredNames[position]
        holder.tvName.text = name

        holder.itemView.setOnClickListener {
            onItemClick(name)
        }
    }

    override fun getItemCount(): Int {
        return filteredNames.size
    }

    class ViewHolder(binding: SubcategoryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.nameCategory
    }

    fun setFilteredList(filteredList: ArrayList<String>) {
        filteredNames.clear()
        filteredNames.addAll(filteredList)
        notifyDataSetChanged()
    }
}