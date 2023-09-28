package tr.main.elephantapps_sprint1.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Brand.BrandModel
import tr.main.elephantapps_sprint1.model.response.Brand.Data
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class SearchBrandsViewModel: ViewModel(){
    val brandsLiveData = MutableLiveData<BrandModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    val brands = MutableLiveData<List<Data>>()

    fun getDataFromAPI(activity:Activity) {
        AppRepo.callApiBrands(activity){ data,success, message ->
            successLiveData.value = success
            brandsLiveData.value  = data

            if (!success){
                errorLiveData.value = message
            }

            brands.value = data?.data
        }
    }
}