package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel

class SigninAndLoginViewModel:ViewModel() {

    var successSigninLiveData = MutableLiveData<Boolean>()
    var errorSigninLiveData = MutableLiveData<String>()

    var successLoginLiveData = MutableLiveData<Boolean>()
    var errorLoginLiveData = MutableLiveData<String>()

    var successLoginWithGoogleLiveData = MutableLiveData<Boolean>()
    var errorLoginWithGoogleLiveData = MutableLiveData<String>()

    fun getStatusCodeForSignin(userModel: UserModel) {
        AppRepo.callApiForSignin(userModel
        ) { success, message ->
            successSigninLiveData.value = success
            errorSigninLiveData.value = message
        }
    }

    fun getStatusCodeForLogin(userLoginModel:UserLoginModel) {

        AppRepo.callApiForLogin(userLoginModel) { success,message ->
            successLoginLiveData.value = success
            errorLoginLiveData.value = message
        }
    }

    fun callApiForLoginWithGoogle(socialAuthenticationModel: SocialAuthenticationModel) {

        AppRepo.callApiForLoginWithGoogle(socialAuthenticationModel) { success,message ->
            successLoginWithGoogleLiveData.value = success
            errorLoginWithGoogleLiveData.value = message
        }
    }
}