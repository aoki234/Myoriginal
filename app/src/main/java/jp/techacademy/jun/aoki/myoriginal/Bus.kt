package jp.techacademy.jun.aoki.myoriginal

import com.google.gson.annotations.SerializedName

data class Bus(
    var direction: String,
    var time: String,
    @SerializedName("has_family")
    val hasFamily: Boolean
)