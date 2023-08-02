package tr.main.elephantapps_sprint1.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.model.request.SendVerificationCodeModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.util.Constans
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
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
                    if (response.isSuccessful){
                        val statusCode = response.code()
                        val success = response.body()!!.success
                        callback(success,response.body()!!.message.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }

        fun callApiForLogin(
            userLoginModel:UserLoginModel,
            callback:(success: Boolean, message: String) -> Unit
        ) {
            apiService.userLogin(userLoginModel,Constans.API_KEY).enqueue(object : Callback<LoginResponseModel> {
                override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                    if(response.isSuccessful){
                        val statusCode = response.code()
                        val success = response.body()!!.success
                        callback(success,response.body()!!.message.toString())
                    }

                }

                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }

        fun callApiForVerifyCode(
            verifyCodeModel: VerifyCodeModel,
            callback: (Int) -> Unit
        ) {
            apiService.verifyCode(verifyCodeModel,Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if(response.body()?.success == true){
                        val statusCode = response.code()
                        callback(statusCode)
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    // Handle the error.
                }
            })
        }

        fun callApiForSendVerificationCode(
            email:String,
            callback:(success: Boolean, message: String) -> Unit
        ) {

            apiService.getVerificationCode(email,Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>,response: Response<ResponseModel>) {
                    if (response.isSuccessful){
                        val statusCode = response.code()
                        val success = response.body()!!.success
                        callback(success,response.body()!!.message.toString())
                    }else{
                        callback(false,response.body()!!.message.toString())
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

            apiService.passwordReset(passwordResetModel,Constans.API_KEY).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>,response: Response<ResponseModel>) {
                    if (response.isSuccessful){
                        val statusCode = response.code()
                        val success = response.body()!!.success
                        callback(success,response.body()!!.message.toString())
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

            apiService.loginwithGoogle(socialAuthenticationModel,Constans.API_KEY,"Bearer"+" "+Constans.TOKEN).enqueue(object : Callback<UserTokenResponseModel> {
                override fun onResponse(call: Call<UserTokenResponseModel>, response: Response<UserTokenResponseModel>) {
                    if (response.isSuccessful){
                        val statusCode = response.code()
                        val success = response.body()!!.success
                        callback(success,response.body()!!.message.toString())
                    }
                }

                override fun onFailure(call: Call<UserTokenResponseModel>, t: Throwable) {
                    callback(false,t.message.toString())
                }
            })
        }
    }






}