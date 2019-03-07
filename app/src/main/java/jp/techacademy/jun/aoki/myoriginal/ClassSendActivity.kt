package jp.techacademy.jun.aoki.myoriginal

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_class_send.*


class ClassSendActivity : AppCompatActivity(), View.OnClickListener, DatabaseReference.CompletionListener {

    private var mGenre: Int = 0
    private lateinit var mClassTitle: ClassTitle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_send)

        // 渡ってきたジャンルの番号を保持する
        val extras = intent.extras
        mGenre = extras.getInt("genre")
        //授業を編集する際に受け取る
        if(extras.containsKey("question")) {
            mClassTitle = extras.get("question") as ClassTitle
            title = "授業編集"

            classtitleText.setText(mClassTitle.title)
            teacherText.setText(mClassTitle.teacher)
            bodyText.setText(mClassTitle.body)

        }else{
            title = "授業投稿"
        }
        // UIの準備
        sendButton.setOnClickListener(this)
    }


    override fun onClick(v: View) {
         if (v === sendButton) {
            // キーボードが出てたら閉じる
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            val dataBaseReference = FirebaseDatabase.getInstance().reference
            val genreRef = dataBaseReference.child(ContentsPATH).child(mGenre.toString())

            val data = HashMap<String, String>()

            // UID
            data["uid"] = FirebaseAuth.getInstance().currentUser!!.uid


            // タイトルと本文を取得する
            val title = classtitleText.text.toString()
             val teacher = teacherText.text.toString()
            val body = bodyText.text.toString()

            if (title.isEmpty()) {
                // タイトルが入力されていない時はエラーを表示するだけ
                Snackbar.make(v, "タイトルを入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

             if (teacher.isEmpty()) {
                 // 質問が入力されていない時はエラーを表示するだけ
                 Snackbar.make(v, "教員名を入力して下さい", Snackbar.LENGTH_LONG).show()
                 return
             }

            if (body.isEmpty()) {
                // 質問が入力されていない時はエラーを表示するだけ
                Snackbar.make(v, "授業内容を入力して下さい", Snackbar.LENGTH_LONG).show()
                return
            }

            // Preferenceから名前を取る
            val sp = PreferenceManager.getDefaultSharedPreferences(this)
            val name = sp.getString(NameKEY, "")

            data["title"] = title
            data["body"] = body
             data["teacher"] = teacher
            data["name"] = name


             if(mClassTitle == null) {
                 genreRef.push().setValue(data, this)
                 Log.d("debug_genre", mGenre.toString())
             }else{
                 genreRef.child(mClassTitle.questionUid).setValue(data,this)
             }
            progressBar.visibility = View.VISIBLE
        }
    }



    override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
        progressBar.visibility = View.GONE

        if (databaseError == null) {
            finish()
        } else {
            Snackbar.make(findViewById(android.R.id.content), "投稿に失敗しました", Snackbar.LENGTH_LONG).show()
        }
    }

}
