package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

public class RecyclerFragment : AppCompatActivity() {

    var TextList: ArrayList<String> = ArrayList()
    private var mRecyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_navigations)

        mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view);

    addDiary()

    mRecyclerView!!.layoutManager = LinearLayoutManager(this)

    mRecyclerView!!.adapter = FragmentAdapter(TextList)

}

fun addDiary() {
    TextList.add("diary1")
    TextList.add("diary2")
    TextList.add("diary3")
    TextList.add("diary4")
    TextList.add("diary5")
    TextList.add("diary6")
    TextList.add("diary7")
    TextList.add("diary8")
    TextList.add("diary10")
    TextList.add("diary11")
    TextList.add("diary12")
    TextList.add("diary13")
}
}
