package jp.techacademy.jun.aoki.myoriginal

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmChangeListener

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

        reloadListView()


        gridview.setOnItemClickListener { parent, view, position, id ->

            val myedit: EditText = EditText(context)
            myedit.requestFocus()
            val dialog = AlertDialog.Builder(context).apply{

            }

            val im = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)

            myedit.setText(mRealm.where(Class::class.java).equalTo("id",position).findFirst()!!.classtitle.toString(), TextView.BufferType.NORMAL)

            dialog.setTitle("授業名を入力してください")
            dialog.setView(myedit)

            dialog.setPositiveButton("OK", { _, positions ->
                // OKボタン押したときの処理
                Log.d("debug",position.toString())


                val userText = myedit.getText().toString()
                Log.d("debug",userText.toString())
                insertclassTitle(userText,position)
                //reloadListView()

            })

            dialog.setNeutralButton("授業レビュー検索",  {_,_ ->
                val intent = Intent(activity,SearchActivity::class.java)
                intent.putExtra("search_word", myedit.getText().toString())
                startActivity(intent)

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

        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mgridAdapter.notifyDataSetChanged()
    }

    private fun insertclassTitle(content:String,i:Int) {
        Log.d("debug","addTaskCalled")
        val zyugyo = Class()
        zyugyo.classtitle = content
        zyugyo.id = i

        mRealm.beginTransaction()
        mRealm.copyToRealmOrUpdate(zyugyo)
        mRealm.commitTransaction()
    }

}

