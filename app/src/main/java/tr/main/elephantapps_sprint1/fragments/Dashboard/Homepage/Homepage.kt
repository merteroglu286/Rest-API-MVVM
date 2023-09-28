package tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.AddProduct
import tr.main.elephantapps_sprint1.adapter.HomeSearch.HomepageViewPagerAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentHomepaegBinding
import tr.main.elephantapps_sprint1.viewmodel.HomeSearchViewModel

class Homepage : Fragment(){

    private lateinit var binding : FragmentHomepaegBinding
    private val viewModel by viewModels<HomeSearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomepaegBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //createSearchBar()
        setViewPager()

        binding.searchBarBeforeTouch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                /*
                val intent = Intent(v.context, SearchActivity::class.java)
                v.context.startActivity(intent)
                this.activity?.overridePendingTransition(
                    R.anim.activity_enter,
                    R.anim.activity_exit
                )

                 */
                binding.viewPager.currentItem = 1
                binding.llSearchBarBeforeTouch.visibility = View.INVISIBLE
                binding.llSearchBarAfterTourch.visibility = View.VISIBLE
                binding.searchBarBeforeTouch.clearFocus()
            }
        }
        binding.ivBackAfterTouch.setOnClickListener {
            binding.viewPager.currentItem = 0
            binding.llSearchBarBeforeTouch.visibility = View.VISIBLE
            binding.llSearchBarAfterTourch.visibility = View.INVISIBLE
        }


        binding.btnAddProduct.setOnClickListener{
            startActivity(Intent(this.activity, AddProduct::class.java))
            this.activity?.overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.container.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    private fun setViewPager(){
        val viewPager2 = binding.viewPager
        val adapter= HomepageViewPagerAdapter(parentFragmentManager,lifecycle)
        viewPager2.adapter=adapter
        viewPager2.isUserInputEnabled = false
    }


    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }


    fun showProgress(){
        binding.viewPager.visibility = View.GONE
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation =
            AnimationUtils.loadAnimation(this@Homepage.context, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }

    fun hideProgress(){
        binding.viewPager.visibility = View.VISIBLE
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }

    private fun createSearchBar(){

        val queryHint = SpannableString("Ürün / Katerogi / Marka / Garaj Ara")
        queryHint.setSpan(
            AbsoluteSizeSpan(14, true),
            0,
            queryHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        queryHint.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            queryHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.searchViewHomepage.queryHint = queryHint
    }
}
