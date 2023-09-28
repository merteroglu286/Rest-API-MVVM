package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.viewpager2.widget.ViewPager2
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityLoginAndSigninBinding
import tr.main.elephantapps_sprint1.adapter.ViewPagerAdapter

class LoginAndSignin : BaseActivity() {

    private lateinit var binding: ActivityLoginAndSigninBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginAndSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPager()

        binding.rgToggle.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.signin -> {
                    binding.viewPager.currentItem = 0

                }

                R.id.login -> {
                    binding.viewPager.currentItem = 1
                }
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    binding.signin.isChecked = true
                } else if (position == 1) {
                    binding.login.isChecked = true
                }
            }
        })

        binding.container.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun setViewPager(){

        val viewPager2 = binding.viewPager

        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)

        viewPager2.adapter=adapter
    }

    fun showProgress(){
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation =
            AnimationUtils.loadAnimation(this@LoginAndSignin, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }
    fun hideProgress(){
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }
}
