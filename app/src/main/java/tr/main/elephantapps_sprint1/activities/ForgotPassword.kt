package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityForgotPasswordBinding
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.ForgotPasswordViewModel

class ForgotPassword : BaseActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMailVerification)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        binding.toolbarMailVerification.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarMailVerification.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtGiveUp.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }
        binding.btnSend.setOnClickListener {
            showProgress()
            sendCode()
        }
        binding.container.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun sendCode(){
        val viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]

        viewModel.callApiForVerificationCode(binding.etEmail.text.toString())

        viewModel.successLiveData.observe(this) { success ->
            if (success == true) {
                hideProgress()
                val intent = Intent(this@ForgotPassword, MailVerification::class.java)
                intent.putExtra("email_sender", EmailSender.ForgotPasswordActivity)
                intent.putExtra("email", binding.etEmail.text.toString())
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
            }
        }

        viewModel.errorLiveData.observe(this) { message ->
            hideProgress()
            Utils.showToast(
                this@ForgotPassword,
                message,
                Toast.LENGTH_SHORT,
                ToastType.Red
            )
        }
    }

    private fun showProgress(){
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation =
            AnimationUtils.loadAnimation(this@ForgotPassword, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }

    private fun hideProgress(){
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }

}