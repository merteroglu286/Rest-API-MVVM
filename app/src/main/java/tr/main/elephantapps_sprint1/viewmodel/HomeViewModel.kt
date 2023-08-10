package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import tr.main.elephantapps_sprint1.model.response.Home.Data
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.service.ApiService

class HomeViewModel :ViewModel(){

    val homeAllLiveData = MutableLiveData<HomeAllResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI() {
        AppRepo.callApiForHome(){ data,success, message ->
            successLiveData.value = success
            errorLiveData.value = message
            homeAllLiveData.value  = data
        }
    }
}