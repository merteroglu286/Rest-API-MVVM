package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class PasswordResetViewModel:ViewModel() {

    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun callApiForPasswordReset(passwordResetModel: PasswordResetModel) {
        AppRepo.callApiForPasswordReset(passwordResetModel
        ) { success, message ->
            successLiveData.value = success
            errorLiveData.value = message
        }
    }
}