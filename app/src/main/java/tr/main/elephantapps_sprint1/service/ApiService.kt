package tr.main.elephantapps_sprint1.service

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.util.Constans
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.model.response.UserTokenResponseModel

interface ApiService {
    @Headers(
        "accept: ${Constans.ACCEPT}",
        "ApiKey: ${Constans.API_KEY}",
        "Content-Type: ${Constans.CONTENT_TYPE}"
    )
    @POST(Constans.EXT_USER_POST)
    fun createUser(
        @Body userModel: UserModel): Call<ResponseModel>

    @POST(Constans.EXT_USER_VERIFICATION)
    fun verifyCode(
        @Body verifyCodeModel: VerifyCodeModel,
        @Header("ApiKey") apiKey: String): Call<ResponseModel>

    @POST(Constans.EXT_AUTHENTICATION_LOGIN)
    fun userLogin(
        @Body userLoginModel: UserLoginModel,
        @Header("ApiKey") apiKey: String): Call<LoginResponseModel>

    @GET(Constans.EXT_USER_SENDVERIFICATION_CODE)
    fun getVerificationCode(
        @Path("email") email: String,
        @Header("ApiKey") apiKey: String): Call<ResponseModel>

    @POST(Constans.EXT_USER_PASSWORD_RESET)
    fun passwordReset(
        @Body passwordResetModel: PasswordResetModel,
        @Header("ApiKey") apiKey: String): Call<ResponseModel>

    @POST(Constans.EXT_AUTHENTICATION_SOCIAL)
    fun loginwithGoogle(
        @Body socialAuthenticationModel: SocialAuthenticationModel,
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String): Call<UserTokenResponseModel>
}