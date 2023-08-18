package tr.main.elephantapps_sprint1.model.request

import tr.main.elephantapps_sprint1.enums.SocialAuthenticationPlatform

data class SocialAuthenticationModel(
    val token: String?,
    val platform: SocialAuthenticationPlatform?
)