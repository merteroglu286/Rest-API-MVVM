package tr.main.elephantapps_sprint1.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.R

class ProductPhotosAdapter(
    private val context: Context,
    private val photoList: ArrayList<Uri>
) :
    RecyclerView.Adapter<ProductPhotosAdapter.ViewHolder>() {

    private val PHOTO_TYPE_COVER = 0
    private val PHOTO_TYPE_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == PHOTO_TYPE_COVER) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout_cover_photo, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout_product_photo, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return photoList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]

        holder.ivDelete.setOnClickListener {
            photoList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, photoList.size)
        }

        Glide.with(context).load(photo).into(holder.image)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_product_photo)
        val ivDelete: ImageView = view.findViewById(R.id.iv_delete)

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return PHOTO_TYPE_COVER
        } else {
            return PHOTO_TYPE_NORMAL
        }

    }
}