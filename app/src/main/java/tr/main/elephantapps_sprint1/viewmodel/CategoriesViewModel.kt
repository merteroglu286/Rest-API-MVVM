package tr.main.elephantapps_sprint1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.model.response.Category.SubCategoryX
import tr.main.elephantapps_sprint1.model.response.Category.SubCategoryXX
import tr.main.elephantapps_sprint1.repository.AppRepo

class CategoriesViewModel:ViewModel(){
    val categoriesLiveData = MutableLiveData<CategoriesResponseModel>()
    var successLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()

    val categories = MutableLiveData<List<Data>>()
    val subCategories = MutableLiveData<List<SubCategory>>()
    val subSubCategories = MutableLiveData<List<SubCategoryX>>()
    val subSubSubCategories = MutableLiveData<List<SubCategoryXX>>()

    fun getDataFromAPI(categoryFilterModel: CategoryFilterModel) {
        AppRepo.callApiCategories(categoryFilterModel){ data, success, message ->
            successLiveData.value = success
            categoriesLiveData.value = data

            if (!success){
                errorLiveData.value = message
            }

            val categoryList = ArrayList<Data>()
            val subCategoryList = ArrayList<SubCategory>()
            val subSubCategoryList = ArrayList<SubCategoryX>()
            val subSubSubCategoryList = ArrayList<SubCategoryXX>()

            data?.let { categoryList.addAll(it.data) }
            for (category in categoryList){
                subCategoryList.addAll(category.subCategories)
            }

            for (subCategory in subCategoryList){
                subSubCategoryList.addAll(subCategory.subCategories)
            }

            for (subsubCategory in subSubCategoryList){
                subSubSubCategoryList.addAll(subsubCategory.subCategories)
            }
            categories.value = categoryList
            subCategories.value = subCategoryList
            subSubCategories.value = subSubCategoryList
            subSubSubCategories.value = subSubSubCategoryList


        }
    }
}