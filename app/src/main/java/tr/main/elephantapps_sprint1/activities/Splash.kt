package tr.main.elephantapps_sprint1.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.util.Utils

class Splash : BaseActivity() {

    private lateinit var ivSplash : ImageView
    private lateinit var tvSplash: TextView
    private  var isLogin: Boolean = false
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var userArrayList: ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        ivSplash = findViewById<ImageView>(R.id.iv_splash)
        tvSplash = findViewById<TextView>(R.id.tv_splash)

        userArrayList = arrayListOf<UserModel>()

        sharedPreferences = getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)

        logoAnim()
        textAnim()

    }
    private fun logoAnim(){
        val logoAnimation = ObjectAnimator.ofFloat(ivSplash, "translationY", 0f, -50f)
        logoAnimation.duration = 2000L
        logoAnimation.start()

        logoAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {

                if (sharedPreferences.getBoolean("isLogin", false)){
                    startActivity(Intent(this@Splash,Dashboard::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@Splash,Onboarding::class.java))
                    finish()
                }
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