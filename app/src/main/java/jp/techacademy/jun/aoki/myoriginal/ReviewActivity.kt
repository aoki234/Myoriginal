package jp.techacademy.jun.aoki.myoriginal

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_review.*

//QuestionDetail
class ReviewActivity : AppCompatActivity() {


    private lateinit var mAdapter: ReviewDetailListAdapter
    private lateinit var mAnswerRef: DatabaseReference

    private lateinit var mClassTitle: ClassTitle
    private var mGenre = 0

    lateinit var mAdView:AdView

    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>

            val answerUid = dataSnapshot.key ?: ""

            for (answer in mClassTitle.answers) {
                // 同じAnswerUidのものが存在しているときは何もしない
                if (answerUid == answer.answerUid) {
                    return
                }
            }

            val body = map["body"] ?: ""
            val name = map["name"] ?: ""
            val interest = map["interest"] ?: ""
            val level = map["level"] ?: ""
            val uid = map["uid"] ?: ""

            val answer = ClassReview(body, name, interest, level, uid, answerUid)
            mClassTitle.answers.add(answer)
            mAdapter.notifyDataSetChanged()
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // 渡ってきたQuestionのオブジェクトを保持する
        val extras = intent.extras
        mClassTitle = extras.get("question") as ClassTitle
        mGenre = extras.getInt("genre")


        title = mClassTitle.title

        // ListViewの準備
        mAdapter = ReviewDetailListAdapter(this, mClassTitle)
        listView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        val mDatabaseReference = FirebaseDatabase.getInstance().reference


        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

       // mAdapter.getItemViewType()

        listView.setOnItemLongClickListener { parent, view, position, id ->

            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val username = sp.getString(NameKEY, "")

            Log.d("debug3",mClassTitle.answers[position-1].toString())
            Log.d("deebug_position",position.toString())

            if (mClassTitle.answers[position-1].name == username){
                val dialog = android.app.AlertDialog.Builder(parent.context).apply{
                }

                dialog.setTitle("登録したこのレビューを削除しますか？")
                dialog.setPositiveButton("OK", { _, positions ->
                    // OKボタン押したときの処理

                    //授業を削除する
                    Log.d("debug2",mClassTitle.answers[position-1].toString())
                    Log.d("debug21",mDatabaseReference.child(ContentsPATH).child(mGenre.toString()).child(mClassTitle.questionUid).toString())

                    Log.d("debug2",mDatabaseReference.child(ContentsPATH).child(mGenre.toString()).child(mClassTitle.questionUid).child("answers").child(mClassTitle.answers[position-1].answerUid).toString())

                    mDatabaseReference.child(ContentsPATH).child(mGenre.toString()).child(mClassTitle.questionUid).child("answers").child(mClassTitle.answers[position-1].answerUid).removeValue()

                    //再度アダプターをセットする
                    mClassTitle.answers.removeAt(position-1)
                    //mAdapter.setQue(mclasstitleArrayList)
                    mAdapter = ReviewDetailListAdapter(this, mClassTitle)
                    listView.adapter = mAdapter
                })


                dialog.setNeutralButton("レビューを編集する",  {_,_ ->
                    //編集するため
                    val intent = Intent(applicationContext, ReviewSendActivity::class.java)
                    intent.putExtra("genre", mGenre)
                    intent.putExtra("question",mClassTitle)
                    intent.putExtra("question_answer",mClassTitle.answers[position-1])
                    startActivity(intent)

                })

                dialog.setNegativeButton("キャンセル", null)
                dialog.show()
            }else {
                Snackbar.make(view,"あなたはこのレビューを削除することはできません。", Snackbar.LENGTH_LONG).show()
            }
            return@setOnItemLongClickListener true
        }

        fab.setOnClickListener {
            // ログイン済みのユーザーを取得する
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                // ログインしていなければログイン画面に遷移させる
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // Questionを渡して回答作成画面を起動する
                val intent = Intent(applicationContext, ReviewSendActivity::class.java)
                intent.putExtra("question", mClassTitle)
                startActivity(intent)
            }
        }


        val dataBaseReference = FirebaseDatabase.getInstance().reference
        mAnswerRef = dataBaseReference.child(ContentsPATH).child(mClassTitle.questionUid).child(AnswersPATH)
        mAnswerRef.addChildEventListener(mEventListener)

    }
}