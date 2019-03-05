package jp.techacademy.jun.aoki.myoriginal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_review.*

//QuestionDetail
class ReviewActivity : AppCompatActivity() {


    private lateinit var mAdapter: ReviewDetailListAdapter
    private lateinit var mAnswerRef: DatabaseReference

    private lateinit var mClassTitle: ClassTitle

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

        title = mClassTitle.title

        // ListViewの準備
        mAdapter = ReviewDetailListAdapter(this, mClassTitle)
        listView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
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