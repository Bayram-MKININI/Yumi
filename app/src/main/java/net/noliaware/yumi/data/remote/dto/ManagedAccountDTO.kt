package net.noliaware.yumi.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import net.noliaware.yumi.domain.model.ManagedAccount

@JsonClass(generateAdapter = true)
data class ManagedAccountDTO(
    @Json(name = "login")
    val login: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "firstname")
    val firstName: String,
    @Json(name = "lastname")
    val lastName: String,
    @Json(name = "cellnumber")
    val cellNumber: String? = null,
    @Json(name = "availablevouchercount")
    val availableVoucherCount: Int
) {
    fun toManagedAccount() = ManagedAccount(
        login = login,
        title = title,
        firstName = firstName,
        lastName = lastName,
        cellNumber = cellNumber,
        availableVoucherCount = availableVoucherCount,
    )
}