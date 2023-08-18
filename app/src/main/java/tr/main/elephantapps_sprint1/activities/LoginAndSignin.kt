package tr.main.elephantapps_sprint1.activities

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityLoginAndSigninBinding
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.Constants.Constans.Companion.RC_SIGN_IN
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.enums.SocialAuthenticationPlatform
import tr.main.elephantapps_sprint1.model.request.Data
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel
import tr.main.elephantapps_sprint1.viewmodel.SigninAndLoginViewModel


class LoginAndSignin : BaseActivity() {


    private lateinit var binding: ActivityLoginAndSigninBinding
    private lateinit var accessToken: String
    private lateinit var customProgressDialog: Dialog
    private var success : Boolean = false

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var userArrayList: ArrayList<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginAndSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userArrayList = arrayListOf<Data>()

        customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.setCancelable(false)

        val fullname = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        if (intent.getIntExtra("finished",0) == 1){
            binding.rgToggle.check(R.id.login)
            binding.etUsername.visibility = View.GONE
            binding.btnSignin.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
            binding.txtForgotPassword.visibility = View.VISIBLE
            binding.txtContract.visibility = View.GONE
        }else{
            binding.rgToggle.check(R.id.signin)
            binding.etUsername.visibility = View.VISIBLE
            binding.btnSignin.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
            binding.txtForgotPassword.visibility = View.GONE
            binding.txtContract.visibility = View.VISIBLE
        }

        binding.rgToggle.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.signin -> {
                    binding.etUsername.visibility = View.VISIBLE
                    binding.btnSignin.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.GONE
                    binding.txtForgotPassword.visibility = View.GONE
                    binding.txtContract.visibility = View.VISIBLE

                }
                R.id.login -> {
                    binding.etUsername.visibility = View.GONE
                    binding.btnSignin.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.txtForgotPassword.visibility = View.VISIBLE
                    binding.txtContract.visibility = View.GONE
                }
            }
        })

        binding.txtForgotPassword.setOnClickListener{
            startActivity(Intent(this@LoginAndSignin,ForgotPassword::class.java))
        }

        binding.btnSignin.setOnClickListener {
            if(binding.etUsername.text.toString() != "" && binding.etEmail.text.toString() != "" && binding.etPassword.text.toString() != ""){
                signIn()
            }
        }


        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.toString() != "" &&
                binding.etPassword.text.toString() != ""){
                login()
            }
        }

        binding.llBtnGoogle.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun signIn(){
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val userModel = UserModel(
            fullName = binding.etUsername.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            isOpenNotification = true
        )


        viewModel.getStatusCodeForSignin(userModel)

        viewModel.successSigninLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()
            if (success == true) {
                val intent = Intent(this@LoginAndSignin,MailVerification::class.java)
                intent.putExtra("email_sender", EmailSender.LoginSigninActivity)
                intent.putExtra("email",binding.etEmail.text.toString())
                startActivity(intent)
            }
        })

        viewModel.errorSigninLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun login(){
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val userLoginModel = UserLoginModel(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        )

        viewModel.getStatusCodeForLogin(userLoginModel)

        viewModel.successLoginLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()

                if (success) {

                    viewModel.loginResponseModelLiveData.observe(this, Observer {data->
                        val user = data.data
                        userArrayList.add(user)
                        sharedPreferences = this.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        val gson = Gson()
                        val json : String = gson.toJson(userArrayList)
                        editor.putString("user",json)
                        editor.putBoolean("isLogin",true)
                        editor.apply()


                        val intent = Intent(this@LoginAndSignin,Dashboard::class.java)
                        startActivity(intent)
                        finish()
                })
            }

        })

        viewModel.errorLoginLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })


    }

    private fun loginWithGoogle(tokenId:String){
        customProgressDialog.show()

        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val socialAuthenticationModel = SocialAuthenticationModel(
            tokenId,
            SocialAuthenticationPlatform.Google
        )

        viewModel.callApiForLoginWithGoogle(socialAuthenticationModel)

        viewModel.successLoginWithGoogleLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()
            if (success == true) {
                val intent = Intent(this@LoginAndSignin,Dashboard::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.errorLoginWithGoogleLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            if (account.idToken != null){
                loginWithGoogle(account.idToken.toString())
            }

        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}
