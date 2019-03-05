package jp.techacademy.jun.aoki.myoriginal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SearchListAdapter(context: Context): BaseAdapter(){

    private var mLayoutInflater: LayoutInflater
    private var mclassTitleArrayList = ArrayList<ClassTitle>()


    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun getCount(): Int {
        return mclassTitleArrayList.size
    }

    override fun getItem(position: Int): Any {
        return mclassTitleArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_reviews, parent, false)
        }

        val titleText = convertView!!.findViewById<View>(R.id.titleTextView) as TextView
        titleText.text = mclassTitleArrayList[position].title

        val teacherText = convertView!!.findViewById<View>(R.id.teacherTextView) as TextView
        teacherText.text = mclassTitleArrayList[position].teacher


        val resText = convertView.findViewById<View>(R.id.resTextView) as TextView
        val resNum = mclassTitleArrayList[position].answers.size
        resText.text = resNum.toString()


        val bodyText = convertView!!.findViewById<View>(R.id.bodyTextView) as TextView
        bodyText.text = mclassTitleArrayList[position].body


        return convertView
    }

    fun setQuestionArrayList(questionArrayList: ArrayList<ClassTitle>) {
        mclassTitleArrayList = questionArrayList
    }
}