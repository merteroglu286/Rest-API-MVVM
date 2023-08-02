package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import tr.main.elephantapps_sprint1.model.request.SendVerificationCodeModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.util.Utils

class ForgotPasswordViewModel:ViewModel() {

    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun callApiForVerificationCode(email: String) {
        AppRepo.callApiForSendVerificationCode(email
        ) { success, message ->
            successLiveData.value = success
            errorLiveData.value = message
        }
    }

}