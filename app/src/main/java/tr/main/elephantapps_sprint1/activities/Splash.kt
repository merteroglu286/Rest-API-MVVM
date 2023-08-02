package tr.main.elephantapps_sprint1.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import tr.main.elephantapps_sprint1.R

class Splash : AppCompatActivity() {

    private lateinit var ivSplash : ImageView
    private lateinit var tvSplash: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        ivSplash = findViewById<ImageView>(R.id.iv_splash)
        tvSplash = findViewById<TextView>(R.id.tv_splash)

        logoAnim()
        textAnim()

    }

    private fun logoAnim(){
        val logoAnimation = ObjectAnimator.ofFloat(ivSplash, "translationY", 0f, -50f)
        logoAnimation.duration = 2000L
        logoAnimation.start()

        logoAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                startActivity(Intent(this@Splash, Onboarding::class.java))
                finish()
            }
        })
    }

    private fun textAnim(){

        val textAnimation = ObjectAnimator.ofFloat(tvSplash, "translationY", 0f, 50f)
        textAnimation.duration = 2000L
        textAnimation.start()

        Handler(Looper.getMainLooper()).postDelayed({
            tvSplash.visibility = View.VISIBLE
        }, 1500)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_text_anim)

        tvSplash.visibility = View.VISIBLE

        tvSplash.startAnimation(fadeInAnimation)

    }
}