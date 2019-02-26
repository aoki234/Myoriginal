package jp.techacademy.jun.aoki.myoriginal

import android.app.Application
import io.realm.Realm

class ClassApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
