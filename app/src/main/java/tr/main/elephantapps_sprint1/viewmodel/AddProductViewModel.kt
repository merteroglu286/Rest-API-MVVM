package tr.main.elephantapps_sprint1.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.ProductRequestModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class AddProductViewModel : ViewModel() {
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI(productRequestModel: ProductRequestModel,activity: Activity) {
        AppRepo.callApiProductRequest(activity,productRequestModel){success, message ->
            successLiveData.value = success
            errorLiveData.value = message
        }
    }
}