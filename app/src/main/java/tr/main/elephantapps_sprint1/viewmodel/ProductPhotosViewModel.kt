package tr.main.elephantapps_sprint1.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class ProductPhotosViewModel:ViewModel() {
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
/*
    fun getDataFromAPI(productPhotosModel: ProductPhotosModel) {
        AppRepo.callApiProductPhotos(productPhotosModel){success, message ->
            successLiveData.value = success
            errorLiveData.value = message
        }
    }

 */
}