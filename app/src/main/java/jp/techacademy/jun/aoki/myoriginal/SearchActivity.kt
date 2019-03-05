package jp.techacademy.jun.aoki.myoriginal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.app_bar_main.*


//MainActivity
class SearchActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mToolbar: Toolbar
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mListView: ListView
    private lateinit var mAdapter: SearchListAdapter

    private lateinit var mclasstitleArrayList: ArrayList<ClassTitle>

    private var mGenreRef: DatabaseReference? = null
    private var mGenre = 0



    private val mEventListener = object : ChildEventListener {

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>
            val title = map["title"] ?: ""
            val body = map["body"] ?: ""
            val teacher = map["teacher"] ?: ""
            val name = map["name"] ?: ""
            val uid = map["uid"] ?: ""


            val answerArrayList = ArrayList<ClassReview>()
            val answerMap = map["answers"] as Map<String, String>?
            if (answerMap != null) {
                for (key in answerMap.keys) {
                    val temp = answerMap[key] as Map<String, String>
                    val answerBody = temp["body"] ?: ""
                    val answerName = temp["name"] ?: ""
                    val answerInterst = temp["interest"] ?: ""
                    val answerLevel = temp["level"] ?: ""
                    val answerUid = temp["uid"] ?: ""
                    val answer = ClassReview(answerBody, answerName,answerInterst,answerLevel, answerUid, key)
                    answerArrayList.add(answer)
                }
            }

            val question = ClassTitle(title, teacher,body,name, uid, dataSnapshot.key ?: "",mGenre, answerArrayList)
            mclasstitleArrayList.add(question)
            Log.d("debug_added",mclasstitleArrayList.toString())
            mAdapter.notifyDataSetChanged()
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>




            // 変更があったQuestionを探す
            for (question in mclasstitleArrayList) {
                if (dataSnapshot.key.equals(question.questionUid)) {
                    // このアプリで変更がある可能性があるのは回答(Answer)のみ
                    question.answers.clear()
                    val answerMap = map["answers"] as Map<String, String>?
                    if (answerMap != null) {
                        for (key in answerMap.keys) {
                            val temp = answerMap[key] as Map<String, String>
                            val answerBody = temp["body"] ?: ""
                            val answerName = temp["name"] ?: ""
                            val answerInterst = temp["interest"] ?: ""
                            val answerLevel = temp["level"] ?: ""
                            val answerUid = temp["uid"] ?: ""
                            val answer = ClassReview(answerBody, answerName,answerInterst,answerLevel, answerUid, key)
                            question.answers.add(answer)
                        }
                    }
                    Log.d("debug","changed data called")
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //setSupportActionBar(toolbar)
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)

        //Log.d("debug",mclasstitleArrayList.toString())


        //toolbar.title = messages.toString()
        //setSupportActionBar(toolbar)


        fab.setOnClickListener { view ->
           // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //   .setAction("Review this class", null).show()

            if (mGenre == 0) {
                Snackbar.make(view, "ジャンルを選択して下さい", Snackbar.LENGTH_LONG).show()
            } else {

        }
            // ログイン済みのユーザーを取得する
            val user = FirebaseAuth.getInstance().currentUser

            // ログインしていなければログイン画面に遷移させる
            if (user == null) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
            }else {

                val intent = Intent(applicationContext, ClassSendActivity::class.java)
                intent.putExtra("genre", mGenre)
                startActivity(intent)
            }
        }

        // ナビゲーションドロワーの設定
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = android.support.v7.app.ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // --- ここから ---
        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().reference

        // ListViewの準備
        mListView = findViewById<ListView>(R.id.listView)
        mAdapter = SearchListAdapter(this)
        mclasstitleArrayList = ArrayList<ClassTitle>()
        mAdapter.notifyDataSetChanged()

        mListView.setOnItemClickListener { parent, view, position, id ->
            // Questionのインスタンスを渡して質問詳細画面を起動する
            val intent = Intent(applicationContext, ReviewActivity::class.java)
            intent.putExtra("question", mclasstitleArrayList[position])
            startActivity(intent)
        }

        mListView.setOnItemLongClickListener { parent, view, position, id ->

            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val username = sp.getString(NameKEY, "")

            Log.d("debug",username.toString())

            if (mclasstitleArrayList[position].name == username){
                val dialog = android.app.AlertDialog.Builder(parent.context).apply{
                }
                dialog.setTitle("登録したこの授業を削除しますか？")
                dialog.setPositiveButton("OK", { _, positions ->
                    // OKボタン押したときの処理

                    //授業を削除する
                    Log.d("debug2",mDatabaseReference.child(ContentsPATH).child(mGenre.toString()).child(mclasstitleArrayList[position].questionUid).toString())
                    mDatabaseReference.child(ContentsPATH).child(mGenre.toString()).child(mclasstitleArrayList[position].questionUid).removeValue()
                })


                dialog.setNeutralButton("授業を編集する",  {_,_ ->
                    //編集するため
                    val intent = Intent(applicationContext, ClassSendActivity::class.java)
                    intent.putExtra("genre", mGenre)
                    startActivity(intent)

                })

                dialog.setNegativeButton("キャンセル", null)
                dialog.show()
            }else {
                Snackbar.make(view,"この授業を削除することはできません。",Snackbar.LENGTH_LONG).show()
            }
                return@setOnItemLongClickListener true

        }
    }


    override fun onResume() {
        super.onResume()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // 1:趣味を既定の選択とする
        if(mGenre == 0) {
            onNavigationItemSelected(navigationView.menu.getItem(0))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_jinka) {
            mToolbar.title = "人間科学部"
            mGenre = 1
            Log.d("debug",mGenre.toString())
        } else if (id == R.id.nav_sports) {
            mToolbar.title = "スポーツ科学部"
            mGenre = 2
        } else if (id == R.id.nav_other) {
            mToolbar.title = "他箇所設置科目"
            mGenre = 3
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        // --- ここから ---
        // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
        mclasstitleArrayList.clear()
        mAdapter.setQuestionArrayList(mclasstitleArrayList)
        Log.d("debug_list",mclasstitleArrayList.toString())
        mListView.adapter = mAdapter

        // 選択したジャンルにリスナーを登録する
        if (mGenreRef != null) {
            mGenreRef!!.removeEventListener(mEventListener)
            Log.d("debug","null genref")
        }
        mGenreRef = mDatabaseReference.child(ContentsPATH).child(mGenre.toString())
        Log.d("debug",mGenreRef.toString())
        mGenreRef!!.addChildEventListener(mEventListener)
        // --- ここまで追加する ---

        return true
    }

}
