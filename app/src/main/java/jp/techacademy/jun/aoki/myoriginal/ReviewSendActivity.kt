package jp.techacademy.jun.aoki.myoriginal

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_review_send.*

class ReviewSendActivity : AppCompatActivity(),DatabaseReference.CompletionListener  {

    private lateinit var mclassTitle: ClassTitle
    private var value_level = 50
    private var value_interest = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_send)


        // 渡ってきたQuestionのオブジェクトを保持する
        val extras = intent.extras
        mclassTitle = extras.get("question") as ClassTitle

        // UIの準備
        //sendButton.setOnClickListener(this)

        var progressBar = findViewById<ProgressBar>(R.id.Progressbar)
        var progressBar_interest = findViewById<ProgressBar>(R.id.Progressbar_interest)

        progressBar.setMax(100)
        //progressBar.setProgress( 81,true)
        var easy_button = findViewById<Button>(R.id.easy)
        easy_button.setOnClickListener{
            value_level -= 10
            progressBar.progress = value_level
            // セカンダリ値
            progressBar.secondaryProgress = 10
        }


        var difficult_button = findViewById<Button>(R.id.difficult)
        difficult_button.setOnClickListener{
            value_level += 10
            // progress
            progressBar.progress = value_level
            // セカンダリ値
            progressBar.secondaryProgress = 10
        }


        progressBar_interest.setMax(100)

        //progressBar.setProgress( 81,true)
        var boring_button = findViewById<Button>(R.id.boring)

        boring_button.setOnClickListener{
            value_interest -= 10
            // progress
            progressBar_interest.progress = value_interest
            // セカンダリ値
            progressBar_interest.secondaryProgress = 10
        }


        var interest_button = findViewById<Button>(R.id.interest)
        interest_button.setOnClickListener{
            value_interest += 10
            // progress
            progressBar_interest.progress = value_interest
            // セカンダリ値
            progressBar_interest.secondaryProgress = 10
        }


        var sendbutton = findViewById<Button>(R.id.sendButton)
        sendbutton.setOnClickListener {
            // キーボードが出てたら閉じる
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            val dataBaseReference = FirebaseDatabase.getInstance().reference
            val answerRef = dataBaseReference.child(ContentsPATH).child(mclassTitle.genre.toString()).child(mclassTitle.questionUid).child(AnswersPATH)

            val data = HashMap<String, String>()

            // UID
            data["uid"] = FirebaseAuth.getInstance().currentUser!!.uid

            // 表示名
            // Preferenceから名前を取る
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val name = sp.getString(NameKEY, "")
            data["name"] = name

            // 回答を取得する
            val answer = answerEditText.text.toString()

            if (answer.isEmpty()) {
                // 回答が入力されていない時はエラーを表示するだけ
                Snackbar.make(it, "回答を入力して下さい", Snackbar.LENGTH_LONG).show()
                //return
            }
            data["body"] = answer

            data["interest"] = value_interest.toString()
            data["level"] = value_level.toString()

            //progressBar.visibility = View.VISIBLE
            answerRef.push().setValue(data, this)
        }
    }


    override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
        //progressBar.visibility = View.GONE

        if (databaseError == null) {
            finish()
        } else {
            Snackbar.make(findViewById(android.R.id.content), "投稿に失敗しました", Snackbar.LENGTH_LONG).show()
        }

    }


}

