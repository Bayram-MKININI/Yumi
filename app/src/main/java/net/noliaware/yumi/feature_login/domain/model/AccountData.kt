package net.noliaware.yumi.feature_login.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AccountData(
    val privacyPolicyUrl: String = "",
    val shouldConfirmPrivacyPolicy: Boolean,
    val helloMessage: String = "",
    val userName: String = "",
    val availableVoucherCount: Int = 0,
    val voucherRequestTypes: List<VoucherRequestType>,
    val messageSubjects: List<MessageSubject>,
    val newAlertCount: Int = 0,
    val newMessageCount: Int = 0,
    val twoFactorAuthMode: TFAMode
) : Parcelable