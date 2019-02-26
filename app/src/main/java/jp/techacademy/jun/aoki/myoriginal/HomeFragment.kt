package jp.techacademy.jun.aoki.myoriginal

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import io.realm.Realm
import io.realm.RealmChangeListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var mgridAdapter: HomeAdapter

    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm> {
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }

    val gridlist = ArrayList<String>()
    val members = arrayOf("yumiko", "yuka", "mai", "nagi", "toko", "kurumi", "miki", "saya", "yuyu", "katakuriko")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_home, container, false)

        // Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        val gridview = v.findViewById<GridView>(R.id.gridview)

        mgridAdapter = HomeAdapter()

        //Log.d("debug",mRealm.where(Class::class.java).findAll().toString())
        // Set the grid view adapter
        gridview.adapter = mgridAdapter

        // Configure the grid view
        gridview.numColumns = 5
        gridview.horizontalSpacing = 2
        gridview.verticalSpacing = 2

        gridview.stretchMode = GridView.STRETCH_COLUMN_WIDTH
        addTaskForTest("Hello")


        gridview.setOnItemClickListener { parent, view, position, id ->
            val myedit: EditText = EditText(context)

            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("授業名を入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", { _, position ->
                // OKボタン押したときの処理
                val userText = myedit.getText().toString()

            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }

        return v
    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }

    private fun reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        val taskRealmResults = mRealm.where(Class::class.java).findAll()

        Log.d("debug",taskRealmResults.toString())


        // 上記の結果を、TaskList としてセットする
        mgridAdapter.list = mRealm.copyFromRealm(taskRealmResults)

        // TaskのListView用のアダプタに渡す
        gridview.adapter = mgridAdapter

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mgridAdapter.notifyDataSetChanged()
    }

    private fun addTaskForTest(content:String) {
        Log.d("debug","addTaskCalled")
        val zyugyo = Class()
        zyugyo.classtitle = content
        zyugyo.id = 2

        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(zyugyo)
        mRealm.commitTransaction()
    }
}

