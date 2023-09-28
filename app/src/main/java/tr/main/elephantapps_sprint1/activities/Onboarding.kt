package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.OnboardingViewPagerAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityOnboardingBinding


class Onboarding : BaseActivity() {
    private var binding : ActivityOnboardingBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val images = listOf(
            R.drawable.tesla,
            R.drawable.tesla,
            R.drawable.tesla
        )

        val viewpager = binding?.vpOnboarding
        val adapter = OnboardingViewPagerAdapter(images)


        viewpager?.adapter = adapter

        binding?.indicator?.setViewPager(viewpager)

        binding?.indicator?.dataSetObserver?.let { adapter.registerDataSetObserver(it) }

        binding?.btnSigninLogin?.setOnClickListener {
            val intent = Intent(this@Onboarding,LoginAndSignin::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}