package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel

class VerifyCodeViewModel: ViewModel() {

    var statusCodeVerifyLiveData = MutableLiveData<Int>()


    fun getStatusCodeForVerify(verifyCodeModel: VerifyCodeModel) {
        AppRepo.callApiForVerifyCode(verifyCodeModel) { statusCode ->
            statusCodeVerifyLiveData.value = statusCode
        }
    }
}