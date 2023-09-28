package tr.main.elephantapps_sprint1.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.Constants.Constans

class ProductPhotoViewPagerAdapter (private val images: List<String>,private val context: Context) : PagerAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(container.context)
        val image = images[position]

        Glide.with(context).load(Constans.PRODUCT_PHOTOS_URL+image).into(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = layoutParams

        container.addView(imageView)

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Remove the ImageView from the ViewPager
        container.removeView(`object` as ImageView)
    }
}