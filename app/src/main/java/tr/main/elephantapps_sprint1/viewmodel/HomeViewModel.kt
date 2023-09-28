package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import tr.main.elephantapps_sprint1.model.response.Home.Category
import tr.main.elephantapps_sprint1.model.response.Home.Data
import tr.main.elephantapps_sprint1.model.response.Home.Garage
import tr.main.elephantapps_sprint1.model.response.Home.Garages
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.model.response.Home.LastAnnouncement
import tr.main.elephantapps_sprint1.model.response.Home.LastEvent
import tr.main.elephantapps_sprint1.model.response.Home.LastVideo
import tr.main.elephantapps_sprint1.model.response.Home.PopularSearch
import tr.main.elephantapps_sprint1.model.response.Home.Product
import tr.main.elephantapps_sprint1.model.response.Home.RandomGlossary
import tr.main.elephantapps_sprint1.model.response.Home.UrgentProduct
import tr.main.elephantapps_sprint1.repository.AppRepo
import tr.main.elephantapps_sprint1.service.ApiService

class HomeViewModel :ViewModel(){
    val homeAllLiveData = MutableLiveData<HomeAllResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    var categories = MutableLiveData<List<Category>>()
    var garages = MutableLiveData<List<Garages>>()
    var products = MutableLiveData<List<Product>>()
    var urgentProducts = MutableLiveData<List<UrgentProduct>>()
    var lastVideo = MutableLiveData<LastVideo>()
    var lastEvent = MutableLiveData<LastEvent>()
    var lastAnnouncement = MutableLiveData<LastAnnouncement>()
    var randomGlossary = MutableLiveData<RandomGlossary>()
    var popularSearch = MutableLiveData<List<PopularSearch>>()

    fun getDataFromAPI() {
        AppRepo.callApiForHome(){ data,success, message ->
            successLiveData.value = success

            val liveData  = data?.data

            if (success){
                if (liveData != null) {
                    homeAllLiveData.value  = data
                    categories.value = liveData.categories
                    garages.value = liveData.garages
                    products.value = liveData.products
                    urgentProducts.value = liveData.urgentProducts
                    lastVideo.value = liveData.lastVideo
                    lastEvent.value = liveData.lastEvent
                    lastAnnouncement.value = liveData.lastAnnouncement
                    randomGlossary.value = liveData.randomGlossary
                    popularSearch.value = liveData.popularSearch
                }
            }else{
                errorLiveData.value = message
            }

        }
    }

}