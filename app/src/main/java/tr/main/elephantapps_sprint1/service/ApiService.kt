package tr.main.elephantapps_sprint1.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.Comment.CommentAddModel
import tr.main.elephantapps_sprint1.model.request.GetProduct.ProductDetailSummaryResponseModel
import tr.main.elephantapps_sprint1.model.request.GetProductByGarage.ProductSummaryResponseModel
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.model.request.ProductRequestModel
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.model.request.Authentication.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.model.response.Brand.BrandModel
import tr.main.elephantapps_sprint1.model.response.Category.CategoriesResponseModel
import tr.main.elephantapps_sprint1.model.response.Comment.ProductCommentsResponseModel
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.model.response.LoginResponseModel
import tr.main.elephantapps_sprint1.model.response.ProductIdResponseModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.model.response.Search.SearchResultResponseModel
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
        @Header("ApiKey") apiKey: String): Call<UserTokenResponseModel>

    @GET(Constans.EXT_HOME_GETALL)
    fun getHomeAll(
        @Header("ApiKey") apiKey: String): Call<HomeAllResponseModel>

    @POST(Constans.EXT_HOME_SEARCH)
    fun getHomeSearch(
        @Body searchModel: SearchModel,
        @Header("ApiKey") apiKey: String): Call<SearchResultResponseModel>

    @POST(Constans.EXT_CATEGORY_GETCATEGORIES)
    fun getCategories(
        @Body categoryModel: CategoryFilterModel,
        @Header("ApiKey") apiKey: String): Call<CategoriesResponseModel>

    @GET(Constans.EXT_BRAND_GETBRANDS)
    fun getBrands(
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String
    ): Call<BrandModel>

    @POST(Constans.EXT_PRODUCT_REQUEST_POST)
    fun postProductRequest(
        @Body productRequestModel: ProductRequestModel,
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String): Call<ResponseModel>

    @POST(Constans.EXT_PRODUCT_POST)
    fun postProduct(
        @Body productAddModel: ProductAddModel,
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String): Call<ProductIdResponseModel>

    @Multipart
    @POST(Constans.EXT_PRODUCT_PHOTOS)
    fun uploadProductPhoto(
        @Part("ProductId") productId: RequestBody,
        @Part productPhoto: MultipartBody.Part,
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String
    ): Call<ResponseModel>

    @POST(Constans.EXT_HOME_SEARCH)
    fun getHomeSearchByCategoryId(
        @Body searchModel: SearchModel,
        @Header("ApiKey") apiKey: String): Call<SearchResultResponseModel>

    @GET(Constans.EXT_PRODUCT_GET)
    fun getProductById(
        @Path("id") id: Int,
        @Header("ApiKey") apiKey: String): Call<ProductDetailSummaryResponseModel>

    @GET(Constans.EXT_PRODUCT_GET_BY_GARAGE)
    fun getProductByGarageId(
        @Path("garageId") id: Int,
        @Header("ApiKey") apiKey: String): Call<ProductSummaryResponseModel>

    @GET(Constans.EXT_PRODUCT_GET_BY_BRAND_ID)
    fun getProductByBrandId(
        @Path("brandId") id: Int,
        @Header("ApiKey") apiKey: String): Call<ProductSummaryResponseModel>

    @GET(Constans.EXT_COMMENT_GET_COMMENTS_OF_PRODUCT)
    fun getCommentByProductId(
        @Query("productId") id: Int,
        @Header("ApiKey") apiKey: String): Call<ProductCommentsResponseModel>

    @POST(Constans.EXT_COMMENT_POST)
    fun postComment(
        @Body commentAddModel: CommentAddModel,
        @Header("ApiKey") apiKey: String,
        @Header("Authorization") authorization: String): Call<ResponseModel>
}