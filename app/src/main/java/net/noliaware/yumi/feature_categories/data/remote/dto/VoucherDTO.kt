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
    @Json(name = "retailerPostcode")
    val retailerPostcode: String?,
    @Json(name = "retailerCity")
    val retailerCity: String?,
    @Json(name = "retailerCountry")
    val retailerCountry: String?,
    @Json(name = "retailerAddressLatitude")
    val retailerAddressLatitude: String?,
    @Json(name = "retailerAddressLongitude")
    val retailerAddressLongitude: String?,
    @Json(name = "retailerPhoneNumber")
    val retailerPhoneNumber: String?,
    @Json(name = "retailerCellPhoneNumber")
    val retailerCellPhoneNumber: String?,
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
        retailerPostcode = retailerPostcode,
        retailerCity = retailerCity,
        retailerCountry = retailerCountry,
        retailerAddressLatitude = retailerAddressLatitude,
        retailerAddressLongitude = retailerAddressLongitude,
        retailerPhoneNumber = retailerPhoneNumber,
        retailerCellPhoneNumber = retailerCellPhoneNumber,
        retailerEmail = retailerEmail,
        retailerWebsite = retailerWebsite,
        partnerDisplayText = partnerDisplayText,
        partnerBannerUrl = partnerBannerUrl,
        partnerBannerAction = partnerBannerAction
    )
}