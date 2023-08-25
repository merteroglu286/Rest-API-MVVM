package tr.main.elephantapps_sprint1.repository

import android.app.Activity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.model.response.Brand.BrandModel
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.model.response.ProductIdResponseModel
import tr.main.elephantapps_sprint1.model.response.Search.SearchResultResponseModel
import tr.main.elephantapps_sprint1.model.response.UserTokenResponseModel
import tr.main.elephantapps_sprint1.util.Utils


class AppRepo {

    companion object{

        private val apiService = Utils.createApiService()
        fun callApiForSignin(
            userModel:UserModel,
            callback:(success:Boolean, message: String) -> Unit
        ) {

            apiService.createUser(userModel).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(true,"")
                            }else{
                                callback(false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(false, "Status code error: $statusCode")
                        }
                    }

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }

        fun callApiForLogin(
            userLoginModel:UserLoginModel,
            callback:(loginResponseModel: LoginResponseModel?,success: Boolean, message: String) -> Unit
        ) {
            apiService.userLogin(userLoginModel, Constans.API_KEY).enqueue(object : Callback<LoginResponseModel> {
                override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body(),true,"")
                            }else{
                                callback(null,false,response.body()!!.message)
                            }
                        }else{
                            callback(null,false, "Status code error: $statusCode")
                        }
                    }
                }
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    callback(null,false,t.message.toString())
                }
            })
        }

        fun callApiForVerifyCode(
            verifyCodeModel: VerifyCodeModel,
            callback:(success: Boolean, message: String) -> Unit
        ) {
            apiService.verifyCode(verifyCodeModel, Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(true,"")
                            }else{
                                callback(false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }

        fun callApiForSendVerificationCode(
            email:String,
            callback:(success: Boolean, message: String) -> Unit
        ) {

            apiService.getVerificationCode(email, Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>,response: Response<ResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(true,"")
                            }else{
                                callback(false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {

                }
            })
        }

        fun callApiForPasswordReset(
            passwordResetModel: PasswordResetModel,
            callback:(success: Boolean, message: String) -> Unit
        ) {

            apiService.passwordReset(passwordResetModel, Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>,response: Response<ResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(true,"")
                            }else{
                                callback(false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }

        fun callApiForLoginWithGoogle(
            socialAuthenticationModel: SocialAuthenticationModel,
            callback:(success: Boolean, message: String) -> Unit
        ) {

            apiService.loginwithGoogle(socialAuthenticationModel, Constans.API_KEY).enqueue(object : Callback<UserTokenResponseModel> {
                override fun onResponse(call: Call<UserTokenResponseModel>, response: Response<UserTokenResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(true,"")
                            }else{
                                callback(false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<UserTokenResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }


        fun callApiForHome(
            callback:(homeAllResponseModel: HomeAllResponseModel?,success: Boolean, message: String) -> Unit
        ) {

            apiService.getHomeAll(Constans.API_KEY).enqueue(object : Callback<HomeAllResponseModel> {
                override fun onResponse(call: Call<HomeAllResponseModel>, response: Response<HomeAllResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body()!!,true,"")
                            }else{
                                callback(null,false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(null,false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<HomeAllResponseModel>, t: Throwable) {
                    callback(null,false,t.message.toString())
                }
            })
        }


        fun callApiHomeSearch(
            homeSearchRequestModel: HomeSearchRequestModel,
            callback:(searchResultResponseModel: SearchResultResponseModel?,success: Boolean, message: String) -> Unit
        ) {

            apiService.getHomeSearch(homeSearchRequestModel, Constans.API_KEY).enqueue(object : Callback<SearchResultResponseModel> {
                override fun onResponse(call: Call<SearchResultResponseModel>, response: Response<SearchResultResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body()!!,true,"")
                            }else{
                                callback(null,false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(null,false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResultResponseModel>, t: Throwable) {
                    callback(null,false,t.message.toString())
                }
            })
        }


        fun callApiCategories(
            categoryFilterModel: CategoryFilterModel,
            callback:(categoriesResponseModel: CategoriesResponseModel?, success: Boolean, message: String) -> Unit
        ) {

            apiService.getCategories(categoryFilterModel, Constans.API_KEY).enqueue(object : Callback<CategoriesResponseModel> {
                override fun onResponse(call: Call<CategoriesResponseModel>, response: Response<CategoriesResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body()!!,true,"")
                            }else{
                                callback(null,false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(null,false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<CategoriesResponseModel>, t: Throwable) {
                    callback(null,false,t.message.toString())
                }
            })
        }


        fun callApiBrands(
            activity:Activity,
            callback:(brandsModel: BrandModel?,success: Boolean, message: String) -> Unit
        ) {

            apiService.getBrands(Constans.API_KEY,"Bearer " + Utils.getUserAccessToken(activity)).enqueue(object : Callback<BrandModel> {
                override fun onResponse(call: Call<BrandModel>, response: Response<BrandModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body()!!,true,"")
                            }else{
                                callback(null,false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(null,false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<BrandModel>, t: Throwable) {
                    callback(null,false,t.message.toString())
                }
            })
        }


        fun callApiProduct(
            activity: Activity,
            productAddModel: ProductAddModel,
            callback:(data:Int, success: Boolean, message: String) -> Unit
        ) {

            apiService.postProduct(productAddModel,Constans.API_KEY,
                "Bearer " + Constans.ACCESS_TOKEN)
                .enqueue(object : Callback<ProductIdResponseModel> {
                override fun onResponse(call: Call<ProductIdResponseModel>, response: Response<ProductIdResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        if (statusCode == 200){
                            val success = response.body()!!.success
                            if (success){
                                callback(response.body()!!.data,true,"")
                            }else{
                                callback(0,false,response.body()!!.message.toString())
                            }
                        }else{
                            callback(0,false, "Status code error: $statusCode")
                        }
                    }
                }

                override fun onFailure(call: Call<ProductIdResponseModel>, t: Throwable) {
                    callback(0,false,t.message.toString())
                }
            })
        }
/*
        fun uploadPhotoAndData(photoPart: MultipartBody.Part,
                               idPart: RequestBody,
                               callback:(success: Boolean, message: String) -> Unit) {
            apiService.postProductPhotos(photoPart, idPart,Constans.API_KEY,"Bearer " + Constans.ACCESS_TOKEN).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if (response.isSuccessful) {
                        callback(true,"")
                    } else {
                        callback(false,response.body()!!.message.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }
*/

    }


}