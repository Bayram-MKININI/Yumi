package net.noliaware.yumi.feature_profile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val login: String?,
    val userReferent: String?,
    val userJob: String?,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val birthDate: String?,
    val birthCity: String?,
    val birthCountry: String?,
    val address: String?,
    val addressComplement: String?,
    val postCode: String?,
    val city: String?,
    val country: String?,
    val phoneNumber: String?,
    val cellPhoneNumber: String?,
    val email: String?,
    val assignedVoucherCount: Int,
    val availableVoucherCount: Int,
    val usedVoucherCount: Int,
    val cancelledVoucherCount: Int,
    val messageBoxUsagePercentage: Int
) : Parcelable