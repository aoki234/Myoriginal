package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.AppLaunchChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mRealm: Realm

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {


            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DashboardFragment()).commit()
                Log.d("debug", "navigation called")
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }


            R.id.navigation_notifications -> {
                // message.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, NavigationFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()

        if (AppLaunchChecker.hasStartedFromLauncher(this)) {
            Log.d("AppLaunchChecker", "2回目以降")

        } else {
            Log.d("AppLaunchChecker", "はじめてアプリを起動した");
            mRealm = Realm.getDefaultInstance()
            addTaskForTest("")

        }

        AppLaunchChecker.onActivityCreate(this)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //set firstfragment
        //supportFragmentManager.beginTransaction().replace(R.id.frameLayout,NavigationFragment()).commit()
    }


    private fun addTaskForTest(content: String) {
        Log.d("debug", "addTaskCalled")
        val zyugyo = Class()

        for (i in 0..24) {
            zyugyo.classtitle = content
            zyugyo.id = i

            mRealm.beginTransaction()
            mRealm.copyToRealmOrUpdate(zyugyo)
            mRealm.commitTransaction()
        }
        mRealm.close()
    }
}
