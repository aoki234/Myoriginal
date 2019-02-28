package jp.techacademy.jun.aoki.myoriginal

import java.io.Serializable
import java.util.*

class ClassTitle(val title: String,val teacher: String, val body: String, val name: String, val uid: String, val questionUid: String,val genre: Int,val answers: ArrayList<ClassReview>) : Serializable {

}