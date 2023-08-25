package tr.main.elephantapps_sprint1.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.ProductRequestModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class AddProductViewModel : ViewModel() {
    var dataLiveData = MutableLiveData<Int>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI(productAddModel: ProductAddModel,activity: Activity) {
        AppRepo.callApiProduct(activity,productAddModel){data,success, message ->
            dataLiveData.value = data
            successLiveData.value = success
            errorLiveData.value = message
        }
    }
}