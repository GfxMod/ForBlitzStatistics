package ru.forblitz.statistics.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Member : Comparable<Member>  {

    @SerializedName("role")
    var role: String? = null

    @JvmField
    @SerializedName("joined_at")
    var joinedAt: String? = null

    @SerializedName("account_id")
    var accountId: String? = null

    @JvmField
    @SerializedName("account_name")
    var accountName: String? = null

    override fun compareTo(other: Member): Int {
        return if (this.role == other.role) {
            if (this.joinedAt != null && other.joinedAt != null) {
                this.joinedAt!!.toInt() - other.joinedAt!!.toInt()
            } else {
                0
            }
        } else if (this.role == "private" && other.role == "executive_officer") {
            1
        } else if (this.role == "private" && other.role == "commander") {
            1
        } else if (this.role == "executive_officer" && other.role == "commander") {
            1
        } else {
            -1
        }
    }

}