package net.noliaware.yumi.feature_categories.domain.model

data class Voucher(
    val voucherId: String,
    val voucherCode: String?,
    val voucherDate: String?,
    val voucherExpiryDate: String?,
    val productLabel: String?,
    val productDescription: String?,
    val productWebpage: String?,
    val retailerType: String?,
    val retailerLabel: String?,
    val retailerAddress: String?,
    val retailerAddressComplement: String?,
    val retailerZipcode: String?,
    val retailerCity: String?,
    val retailerAddressAltitude: String?,
    val retailerAddressLongitude: String?,
    val retailerPhone: String?,
    val retailerMobile: String?,
    val retailerEmail: String?,
    val retailerWebsite: String?,
    val partnerDisplayText: String?,
    val partnerBannerUrl: String?,
    val partnerBannerAction: String?
)