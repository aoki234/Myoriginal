package jp.techacademy.jun.aoki.myoriginal

import java.io.Serializable

class Bus(
    var direction: String,
    var time: String
    //@SerializedName("has_family")
    //val hasFamily: Boolean
): Serializable