package tr.main.elephantapps_sprint1.Constants

class Constans {
    companion object{
        const val BASE_URL = "https://tomofilya.azurewebsites.net/"
        const val EXT_USER_POST = "User/Post"
        const val EXT_USER_VERIFICATION = "User/VerifyCode"
        const val EXT_AUTHENTICATION_LOGIN = "Authentication/Login"
        const val EXT_USER_SENDVERIFICATION_CODE = "User/SendVerificationCode/{email}"
        const val EXT_USER_PASSWORD_RESET = "User/PasswordReset"
        const val EXT_AUTHENTICATION_SOCIAL = "Authentication/Social"
        const val EXT_HOME_GETALL = "Home/GetAll"
        const val EXT_HOME_SEARCH = "Home/Search"
        const val EXT_CATEGORY_GETCATEGORIES = "Category/GetCategories"
        const val EXT_BRAND_GETBRANDS = "Brand/GetBrands"
        const val EXT_PRODUCT_REQUEST_POST = "Product/ProductRequestPost"
        const val EXT_PRODUCT_POST = "Product/Post"
        const val EXT_PRODUCT_PHOTOS = "Product/ProductPhotosPost"

        const val API_KEY = "COF40RZ95GBJZ7R08QVJMIDR0TLEJL1DDEXY10K0H8MQ03DJJ8"
        const val ACCEPT = "application/json"
        const val CONTENT_TYPE = "application/json"

        const val  RC_SIGN_IN = 9001

        const val CATEGORY_ICONS_URL = "https://tomofilyastorage.blob.core.windows.net/categoryicons/"
        const val GARAGE_LOGOS_URL = "https://tomofilyastorage.blob.core.windows.net/garagelogos/"
        const val PRODUCT_PHOTOS_URL = "https://tomofilyastorage.blob.core.windows.net/productphotos/"

        const val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6Inl1c3VmdG9tb2ZpbHlheXVydGljaUBnbWFpbC5jb20iLCJuYW1laWQiOiI0NjQiLCJlbWFpbCI6Inl1c3VmdG9tb2ZpbHlheXVydGljaUBnbWFpbC5jb20iLCJuYmYiOjE2OTI2OTczNjAsImV4cCI6MTY5MzMwMjE2MCwiaWF0IjoxNjkyNjk3MzYwfQ.HL5s_u6hiPxfySAaJdQ75fiOmeTYuKDGV7VWSHOJ_kA"

        const val TURKISH_LIRA_SYMBOL = "\u20BA"

        const val FILE_NAME_FORMAT = "yy-MM-dd-HH-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 123

    }
}
