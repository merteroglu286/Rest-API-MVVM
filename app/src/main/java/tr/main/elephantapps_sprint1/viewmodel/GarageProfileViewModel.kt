package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.GetProductByGarage.Data
import tr.main.elephantapps_sprint1.repository.AppRepo

class GarageProfileViewModel:ViewModel() {

    val data = MutableLiveData<List<Data>>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    //val campaignLiveData = MutableLiveData<Campaign>()

    fun getDataFromAPI(id: Int) {
        AppRepo.callApiGetProductByGarageId(id){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message

            if (successLiveData.value == true){
                this.data.value = data!!.data
            }
        }
    }
}