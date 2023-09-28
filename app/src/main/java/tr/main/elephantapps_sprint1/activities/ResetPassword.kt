package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityResetPasswordBinding
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.PasswordResetViewModel

class ResetPassword : BaseActivity() {
    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var email: String
    private lateinit var code: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarResetPassword)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarResetPassword.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarResetPassword.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtGiveUp.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        email = intent.getStringExtra("email").toString()
        code = intent.getStringExtra("code").toString()

        binding.btnSend.setOnClickListener {
            showProgress()
            if(checkValues()){
                changePassword()
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
        val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun changePassword(){
        val viewModel = ViewModelProvider(this)[PasswordResetViewModel::class.java]

        val passwordResetMode = PasswordResetModel(
            email,
            code,
            binding.etPassword.text.toString()
        )

        viewModel.callApiForPasswordReset(passwordResetMode)

        viewModel.successLiveData.observe(this) { success ->
            if (success == true) {
                hideProgress()
                Utils.showToast(this@ResetPassword, "Parola başarıyla değiştirildi", Toast.LENGTH_SHORT, ToastType.Green)
                val intent = Intent(this@ResetPassword, LoginAndSignin::class.java)
                intent.putExtra("finished", 1)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                finish()
            }
        }

        viewModel.errorLiveData.observe(this) { message ->
            hideProgress()
            Utils.showToast(this@ResetPassword, message, Toast.LENGTH_SHORT, ToastType.Red)

        }
    }

    private fun checkValues(): Boolean {
        return if (
            binding.etPassword.text.isNullOrEmpty() ||
            binding.etPasswordAgain.text.isNullOrEmpty() ||
            binding.etPassword.text.toString()  != binding.etPasswordAgain.text.toString()
        ){
            hideProgress()
            Utils.showToast(this@ResetPassword,"Lütfen parolanızı doğru giriniz",Toast.LENGTH_SHORT,ToastType.Yellow)
            false
        }else{
            true
        }
    }

    private fun showProgress(){
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation =
            AnimationUtils.loadAnimation(this@ResetPassword, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }

    private fun hideProgress(){
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }
}