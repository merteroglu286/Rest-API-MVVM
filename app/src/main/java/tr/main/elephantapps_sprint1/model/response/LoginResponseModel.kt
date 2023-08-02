package tr.main.elephantapps_sprint1.model.response

import tr.main.elephantapps_sprint1.model.request.Data

data class LoginResponseModel(
    val `data`: Data,
    val message: String,
    val success: Boolean
)