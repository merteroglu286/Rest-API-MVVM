package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.internal.notifyAll
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.model.response.Search.Brand
import tr.main.elephantapps_sprint1.model.response.Search.Category
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.model.response.Search.SearchResultResponseModel
import tr.main.elephantapps_sprint1.repository.AppRepo

class HomeSearchViewModel:ViewModel() {
    val homeSearchLiveData = MutableLiveData<SearchResultResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    var categories = MutableLiveData<List<Category>?>()
    var brands = MutableLiveData<List<Brand>?>()
    var garages = MutableLiveData<List<Garage>?>()
    var products = MutableLiveData<List<Product>?>()

    fun getDataFromAPI(searchModel: SearchModel) {
        AppRepo.callApiHomeSearch(searchModel){ data, success, message ->

            successLiveData.value = success
            homeSearchLiveData.value = data
            brands.value = data?.data?.brands
            categories.value = data?.data?.categories
            garages.value = data?.data?.garages
            products.value = data?.data?.products

            if (!success){
                errorLiveData.value = message
            }
        }
    }

}