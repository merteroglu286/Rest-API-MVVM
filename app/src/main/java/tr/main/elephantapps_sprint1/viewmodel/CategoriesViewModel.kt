package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class CategoriesViewModel:ViewModel(){
    val categoriesLiveData = MutableLiveData<CategoriesResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    fun getDataFromAPI(categoryFilterModel: CategoryFilterModel) {
        AppRepo.callApiCategories(categoryFilterModel){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message
            categoriesLiveData.value = data
        }
    }
}