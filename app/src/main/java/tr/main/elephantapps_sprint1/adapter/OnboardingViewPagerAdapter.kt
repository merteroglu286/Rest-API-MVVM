package tr.main.elephantapps_sprint1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import tr.main.elephantapps_sprint1.R

class OnboardingViewPagerAdapter(private val images: List<Int>) : PagerAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Create a ImageView
        val imageView = ImageView(container.context)

        // Set the image to the ImageView
        imageView.setImageResource(images[position])

        // Add the ImageView to the ViewPager
        container.addView(imageView)

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Remove the ImageView from the ViewPager
        container.removeView(`object` as ImageView)
    }
}