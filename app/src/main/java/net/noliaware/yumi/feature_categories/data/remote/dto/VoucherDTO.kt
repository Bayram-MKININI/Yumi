package net.noliaware.yumi.feature_categories.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.feature_categories.domain.model.Voucher

@JsonClass(generateAdapter = true)
data class VoucherDTO(
    @Json(name = "voucherId")
    val voucherId: String,
    @Json(name = "voucherCode")
    val voucherCode: String?,
    @Json(name = "voucherDate")
    val voucherDate: String?,
    @Json(name = "voucherExpiryDate")
    val voucherExpiryDate: String?,
    @Json(name = "productLabel")
    val productLabel: String?,
    @Json(name = "productDescription")
    val productDescription: String?,
    @Json(name = "productWebpage")
    val productWebpage: String?,
    @Json(name = "retailerType")
    val retailerType: String?,
    @Json(name = "retailerLabel")
    val retailerLabel: String?,
    @Json(name = "retailerAddress")
    val retailerAddress: String?,
    @Json(name = "retailerAddressComplement")
    val retailerAddressComplement: String?,
    @Json(name = "retailerZipcode")
    val retailerZipcode: String?,
    @Json(name = "retailerCity")
    val retailerCity: String?,
    @Json(name = "retailerAddressLatitude")
    val retailerAddressAltitude: String?,
    @Json(name = "retailerAddressLongitude")
    val retailerAddressLongitude: String?,
    @Json(name = "retailerPhone")
    val retailerPhone: String?,
    @Json(name = "retailerMobile")
    val retailerMobile: String?,
    @Json(name = "retailerEmail")
    val retailerEmail: String?,
    @Json(name = "retailerWebsite")
    val retailerWebsite: String?,
    @Json(name = "partnerDisplayText")
    val partnerDisplayText: String?,
    @Json(name = "partnerBannerUrl")
    val partnerBannerUrl: String?,
    @Json(name = "partnerBannerAction")
    val partnerBannerAction: String?
) {
    fun toVoucher() = Voucher(
        voucherId = voucherId,
        voucherCode = voucherCode,
        voucherDate = voucherDate,
        voucherExpiryDate = voucherExpiryDate,
        productLabel = productLabel,
        productDescription = productDescription,
        productWebpage = productWebpage,
        retailerType = retailerType,
        retailerLabel = retailerLabel,
        retailerAddress = retailerAddress,
        retailerAddressComplement = retailerAddressComplement,
        retailerZipcode = retailerZipcode,
        retailerCity = retailerCity,
        retailerAddressAltitude = retailerAddressAltitude,
        retailerAddressLongitude = retailerAddressLongitude,
        retailerPhone = retailerPhone,
        retailerMobile = retailerMobile,
        retailerEmail = retailerEmail,
        retailerWebsite = retailerWebsite,
        partnerDisplayText = partnerDisplayText,
        partnerBannerUrl = partnerBannerUrl,
        partnerBannerAction = partnerBannerAction
    )
}