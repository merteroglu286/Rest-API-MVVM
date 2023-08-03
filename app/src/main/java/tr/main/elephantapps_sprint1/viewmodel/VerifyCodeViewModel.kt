package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel

class VerifyCodeViewModel: ViewModel() {

    var successVerifyLiveData = MutableLiveData<Boolean>()
    var errorVerifyLiveData = MutableLiveData<String>()


    fun getStatusCodeForVerify(verifyCodeModel: VerifyCodeModel) {
        AppRepo.callApiForVerifyCode(verifyCodeModel) { success,message ->
            successVerifyLiveData.value = success
            errorVerifyLiveData.value = message
        }
    }
}