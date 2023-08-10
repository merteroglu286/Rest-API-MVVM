package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.model.response.Search.SearchResultResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class HomeSearchViewModel:ViewModel() {
    val homeSearchLiveData = MutableLiveData<SearchResultResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI(homeSearchRequestModel: HomeSearchRequestModel) {
        AppRepo.callApiHomeSearch(homeSearchRequestModel){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message
            homeSearchLiveData.value = data
        }
    }
}