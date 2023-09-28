package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.GetProduct.Brand
import tr.main.elephantapps_sprint1.model.request.GetProduct.Comment
import tr.main.elephantapps_sprint1.model.request.GetProduct.Garage
import tr.main.elephantapps_sprint1.model.request.GetProduct.GarageProduct
import tr.main.elephantapps_sprint1.model.request.GetProduct.SimularProduct
import tr.main.elephantapps_sprint1.repository.AppRepo

class ProductDetailViewModel:ViewModel() {

    val data = MutableLiveData<tr.main.elephantapps_sprint1.model.request.GetProduct.Data>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    val garageLiveData = MutableLiveData<Garage>()
    val productPhotoUrls = MutableLiveData<List<String>>()
    val similarProducts = MutableLiveData<List<SimularProduct>>()
    val garageProducts = MutableLiveData<List<GarageProduct>>()
    val brandLiveData = MutableLiveData<Brand>()
    val commentLiveData = MutableLiveData<List<Comment>>()

    val productData = MutableLiveData<List<tr.main.elephantapps_sprint1.model.request.GetProductByGarage.Data>>()

    fun getDataFromAPI(id: Int) {
        AppRepo.callApiGetProductById(id){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message

            if (successLiveData.value == true){
                this.data.value = data
                this.garageLiveData.value = data!!.garage
                this.productPhotoUrls.value = data.productPhotoUrls
                this.similarProducts.value = data.simularProducts
                this.garageProducts.value = data.garageProducts
                this.brandLiveData.value = data.brand
                this.commentLiveData.value = data.comments
            }
        }
    }

    fun getProductDataByBrandId(id:Int){
        AppRepo.callApiGetProductByBrandId(id){ data, success, message ->
            successLiveData.value = success
            errorLiveData.value = message

            if (successLiveData.value == true){
                if (data != null) {
                    this.productData.value = data.data
                }
            }
        }
    }
}