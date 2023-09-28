package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.model.request.Authentication.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel

class SigninAndLoginViewModel:ViewModel() {

    var successSigninLiveData = MutableLiveData<Boolean>()
    var errorSigninLiveData = MutableLiveData<String>()

    var loginResponseModelLiveData = MutableLiveData<LoginResponseModel>()
    var successLoginLiveData = MutableLiveData<Boolean>()
    var errorLoginLiveData = MutableLiveData<String>()

    var successLoginWithGoogleLiveData = MutableLiveData<Boolean>()
    var errorLoginWithGoogleLiveData = MutableLiveData<String>()

    fun getStatusCodeForSignin(userModel: UserModel) {
        AppRepo.callApiForSignin(userModel
        ) { success, message ->
            successSigninLiveData.value = success
            if (!success){
                errorSigninLiveData.value = message
            }
        }
    }

    fun getStatusCodeForLogin(userLoginModel: UserLoginModel) {

        AppRepo.callApiForLogin(userLoginModel) { data,success,message ->
            successLoginLiveData.value = success

            if (success){
                loginResponseModelLiveData.value = data
            }
            if (!success){
                errorLoginLiveData.value = message
            }

        }
    }

    fun callApiForLoginWithGoogle(socialAuthenticationModel: SocialAuthenticationModel) {

        AppRepo.callApiForLoginWithGoogle(socialAuthenticationModel) { success,message ->
            successLoginWithGoogleLiveData.value = success
            if (!success){
                errorLoginWithGoogleLiveData.value = message
            }
        }
    }
}