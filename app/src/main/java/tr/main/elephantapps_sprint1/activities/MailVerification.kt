package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityMailVerificationBinding
import tr.main.elephantapps_sprint1.databinding.DialogMailVerificationBinding
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.ForgotPasswordViewModel
import tr.main.elephantapps_sprint1.viewmodel.VerifyCodeViewModel

class MailVerification : BaseActivity() {

    private lateinit var binding: ActivityMailVerificationBinding
    private lateinit var email: String
    private var timer: CountDownTimer? = null
    private val initialTimeInMillis: Long = 60000

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMailVerificationBinding.inflate(layoutInflater)
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
/*
        binding.txtSendCodeAgain.setOnClickListener{
            Toast.makeText(this,"Kod yeniden gönderilecek",Toast.LENGTH_SHORT).show()
        }

 */
        email = intent.getStringExtra("email").toString()

        startTimer()

        binding.btnSend.setOnClickListener {
            if (checkValue()){
                showProgress()
                callApiForVerifyCode()
            }
        }

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

    private fun startTimer(){
        timer?.cancel()

        timer = object : CountDownTimer(initialTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                binding.txtTime.text = timeLeftFormatted
            }

            override fun onFinish() {
                Utils.showToast(
                    this@MailVerification,
                    "Zaman aşımına uğradınız. Lütfen tekrardan kod gönderiniz",
                    Toast.LENGTH_LONG,
                    ToastType.Red)

                createDialog()
            }
        }
        timer?.start()
    }

    private fun  createDialog(){
        val dialogBinding = DialogMailVerificationBinding.inflate(LayoutInflater.from(this))
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(dialogBinding.root)
        val dialog = alertDialog.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.SlideUpDialogAnimation
        dialog.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnSendAgain.setOnClickListener {
            timer?.start()
            dialog.dismiss()
            sendCodeAgain()
        }
        dialogBinding.tvCancel.setOnClickListener {
            dialog.dismiss()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
            finish()
        }
    }

    private fun sendCodeAgain() {

        val viewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]

        viewModel.callApiForVerificationCode(email)

        viewModel.successLiveData.observe(this) { success ->
            if (success == true) {
                hideProgress()
            }
        }

        viewModel.errorLiveData.observe(this) { message ->
            hideProgress()
            Utils.showToast(
                this@MailVerification,
                message,
                Toast.LENGTH_SHORT,
                ToastType.Red
            )
        }

    }

    private fun callApiForVerifyCode(){
        val viewModel = ViewModelProvider(this)[VerifyCodeViewModel::class.java]

        val verifyCodeModel = VerifyCodeModel(
            email,
            binding.pinview.text.toString()
        )

        viewModel.getStatusCodeForVerify(verifyCodeModel)

        viewModel.successVerifyLiveData.observe(this) { success ->
            if (success == true) {
                hideProgress()
                when (intent.getSerializableExtra("email_sender")) {
                    EmailSender.LoginSigninActivity -> {
                        val intent = Intent(this@MailVerification, Welcome::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                        timer?.cancel()
                        timer = null
                        finish()
                    }

                    EmailSender.ForgotPasswordActivity -> {
                        val intent = Intent(this@MailVerification, ResetPassword::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("code", binding.pinview.text.toString())
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                        finish()
                    }
                }
            }
        }

        viewModel.errorVerifyLiveData.observe(this) { message ->
            if (message != ""){
                hideProgress()
                Utils.showToast(this@MailVerification, message, Toast.LENGTH_SHORT, ToastType.Red)
            }
        }
    }

    private fun checkValue(): Boolean {
        return if(binding.pinview.text.isNullOrEmpty() || binding.pinview.text!!.length < 4){
            Utils.showToast(this@MailVerification, "Lütfen kod alanını eksiksiz doldurunuz", Toast.LENGTH_SHORT, ToastType.Red)
            false
        }else{
            true
        }
    }

    private fun showProgress(){
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation =
            AnimationUtils.loadAnimation(this@MailVerification, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }

    private fun hideProgress(){
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }
}