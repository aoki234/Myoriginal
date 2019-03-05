package jp.techacademy.jun.aoki.myoriginal

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

//QuestionDetailListAdapter
class ReviewDetailListAdapter(context: Context, private val mclassTitle: ClassTitle) : BaseAdapter() {
    companion object {
        private val TYPE_QUESTION = 0
        private val TYPE_ANSWER = 1
    }

    private var mLayoutInflater: LayoutInflater? = null

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return 1 + mclassTitle.answers.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_QUESTION
        } else {
            TYPE_ANSWER
        }
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Any {
        return mclassTitle
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (getItemViewType(position) == TYPE_QUESTION) {
            if (convertView == null) {
                convertView = mLayoutInflater!!.inflate(R.layout.list_reviews_detail, parent, false)!!
            }
            val body = mclassTitle.title
            val name = mclassTitle.teacher

            val bodyTextView = convertView.findViewById<View>(R.id.classtitleTextView) as TextView
            bodyTextView.text = body

            val nameTextView = convertView.findViewById<View>(R.id.teacherTextView) as TextView
            nameTextView.text = name

        } else {
            if (convertView == null) {
                convertView = mLayoutInflater!!.inflate(R.layout.list_answer, parent, false)!!
            }

            val answer = mclassTitle.answers[position - 1]
            val body = answer.body
            val interest = answer.interest
            val level = answer.level
            val name = answer.name

            Log.d("debug",interest.toString())

            val bodyTextView = convertView.findViewById<View>(R.id.bodyTextView) as TextView
            bodyTextView.text = body

            val levelTextView = convertView.findViewById<View>(R.id.levelTextView) as TextView
            levelTextView.text = level.toString()

            val interestTextView = convertView.findViewById<View>(R.id.interestTextView) as TextView
            interestTextView.text = interest.toString()

            val nameTextView = convertView.findViewById<View>(R.id.nameTextView) as TextView
            nameTextView.text = name
        }

        return convertView
    }
}