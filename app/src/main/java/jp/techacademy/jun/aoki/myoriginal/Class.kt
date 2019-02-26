package jp.techacademy.jun.aoki.myoriginal

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Class : RealmObject(), Serializable {
    var classtitle: String = ""      // タイトル
    //var color: Int = "".toInt()
    //var teacher: String = ""        //先生
    //var category: String = ""     //属性
    //var contents: String = ""   // 内容

    // id をプライマリーキーとして設定
    @PrimaryKey
    var id: Int = 0
}
