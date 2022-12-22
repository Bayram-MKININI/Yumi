package net.noliaware.yumi.feature_profile.domain.model

data class BOSignIn(
    val expiryDelayInSeconds: Int,
    val signInCode: String
)