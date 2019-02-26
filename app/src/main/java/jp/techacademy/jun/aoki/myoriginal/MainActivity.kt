package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {


            R.id.navigation_dashboard -> {
                //message.setText(R.string.title_dashboard)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout,DashboardFragment()).commit()
                Log.d("debug","navigation called")
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_home -> {
                //message.setText(R.string.title_home)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout,HomeFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }


            R.id.navigation_notifications -> {
               // message.setText(R.string.title_notifications)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout,NavigationFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,HomeFragment()).commit()


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        //set firstfragment
        //supportFragmentManager.beginTransaction().replace(R.id.frameLayout,NavigationFragment()).commit()
    }
}
