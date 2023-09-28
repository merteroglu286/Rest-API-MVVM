package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.HomeSearchByCategoryModel
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Search.SearchResultResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class FilterProductViewModel:ViewModel() {


    val productLiveData = MutableLiveData<SearchResultResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI(searchModel: SearchModel) {
        AppRepo.callApiProductByCategory(searchModel){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message
            productLiveData.value = data
        }
    }
}