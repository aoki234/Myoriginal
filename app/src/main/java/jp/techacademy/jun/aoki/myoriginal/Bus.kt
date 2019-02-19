package jp.techacademy.jun.aoki.myoriginal

import java.io.Serializable

data class Bus(
    var direction: String,
    var time: String
    //@SerializedName("has_family")
    //val hasFamily: Boolean
): Serializable