package tr.main.elephantapps_sprint1.fragments.LoginAndSignin


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.Constants.SharedPreferenceConstants
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.Dashboard
import tr.main.elephantapps_sprint1.activities.LoginAndSignin
import tr.main.elephantapps_sprint1.activities.MailVerification
import tr.main.elephantapps_sprint1.databinding.FragmentSigninBinding
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.enums.SocialAuthenticationPlatform
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.request.Data
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.SigninAndLoginViewModel


class Signin : Fragment() {
    private lateinit var binding : FragmentSigninBinding
    private var isChecked = false
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var userArrayList: ArrayList<Data>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userArrayList = arrayListOf()

        binding.roundCheckbox.setOnClickListener {
            isChecked = !isChecked
            updateCheckboxState()
        }

        binding.btnSignin.setOnClickListener {
            if(checkValues()){
                showParentProgress()
                signIn()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        binding.llBtnGoogle.setOnClickListener {
            showParentProgress()
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Constans.RC_SIGN_IN)
        }

        binding.container.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun updateCheckboxState() {
        if (isChecked) {
            binding.roundCheckbox.setImageResource(R.drawable.light_bg_round_check)
        } else {
            binding.roundCheckbox.setImageResource(R.drawable.white_outlined_bg_full_round)
        }
    }


    private fun signIn(){
        val viewModel = ViewModelProvider(this)[SigninAndLoginViewModel::class.java]

        val userModel = UserModel(
            fullName = binding.etUsername.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            isOpenNotification = true
        )

        viewModel.getStatusCodeForSignin(userModel)

        viewModel.successSigninLiveData.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                hideParentProgress()
                Utils.showToast(
                    requireContext(),
                    getString(R.string.message_successful),
                    Toast.LENGTH_SHORT,
                    ToastType.Green
                )

                sharedPreferences = requireContext().getSharedPreferences(
                    SharedPreferenceConstants.USER_INFO, Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val json: String = gson.toJson(userArrayList)
                editor.putString("user",json)
                editor.putBoolean("isLogin", true)
                editor.apply()


                val intent = Intent(requireActivity(), MailVerification::class.java)
                intent.putExtra("email_sender", EmailSender.LoginSigninActivity)
                intent.putExtra("email", binding.etEmail.text.toString())
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    R.anim.activity_enter,
                    R.anim.activity_exit
                )
            }
        }

        viewModel.errorSigninLiveData.observe(viewLifecycleOwner) { message ->
            hideParentProgress()
            Utils.showToast(requireContext(), message, Toast.LENGTH_SHORT, ToastType.Red)
        }
    }

    private fun checkValues(): Boolean {
        return if (binding.etUsername.text.isNullOrEmpty() ||
            binding.etEmail.text.isNullOrEmpty() ||
            binding.etPassword.text.isNullOrEmpty()){
            Utils.showToast(requireContext(),null,Toast.LENGTH_SHORT,ToastType.Yellow)
            false
        }else{
            true
        }
    }


    private fun loginWithGoogle(tokenId:String){

        val viewModel = ViewModelProvider(this)[SigninAndLoginViewModel::class.java]

        val socialAuthenticationModel = SocialAuthenticationModel(
            tokenId,
            SocialAuthenticationPlatform.Google
        )

        viewModel.callApiForLoginWithGoogle(socialAuthenticationModel)

        viewModel.successLoginWithGoogleLiveData.observe(this) { success ->
            if (success == true) {

                hideParentProgress()

                Utils.showToast(requireContext(),getString(R.string.message_successful),Toast.LENGTH_SHORT,ToastType.Green)
                sharedPreferences = requireContext().getSharedPreferences(
                    SharedPreferenceConstants.USER_INFO, Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val json: String = gson.toJson(userArrayList)
                editor.putString("user",json)
                editor.putBoolean("isLogin", true)
                editor.apply()


                val intent = Intent(requireActivity(), Dashboard::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                requireActivity().finish()
            }
        }

        viewModel.errorLoginWithGoogleLiveData.observe(this) { message ->
            hideParentProgress()
            Utils.showToast(requireContext(), message, Toast.LENGTH_SHORT, ToastType.Red)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constans.RC_SIGN_IN) {
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
            hideParentProgress()
            Utils.showToast(requireContext(),"Hata kodu: " +  e.statusCode, Toast.LENGTH_SHORT, ToastType.Yellow)
        }
    }

    private fun showParentProgress(){
        val parentActivity = activity as? LoginAndSignin
        parentActivity?.showProgress()
    }

    private fun hideParentProgress(){
        val parentActivity = activity as? LoginAndSignin
        parentActivity?.hideProgress()
    }

}