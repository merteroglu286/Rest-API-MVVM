package tr.main.elephantapps_sprint1.model.response

import tr.main.elephantapps_sprint1.model.request.Data

data class UserTokenResponseModel (
    val `data`: Data,
    val message: String,
    val success: Boolean
)