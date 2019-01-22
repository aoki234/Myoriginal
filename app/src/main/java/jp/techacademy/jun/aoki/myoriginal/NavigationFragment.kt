package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class NavigationFragment : Fragment() {

    var TextList: ArrayList<String> = ArrayList()
    private var mRecyclerView: RecyclerView? = null
    private var mDirectionButton: Button? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_navigations, container, false)

        mDirectionButton = v.findViewById(R.id.button1)
        mRecyclerView = v.findViewById(R.id.recycler_view)

        addDiary()

        Log.d("debug",TextList.toString())

        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)

        Log.d("deug",mRecyclerView!!.layoutManager.toString())

        mRecyclerView!!.adapter = FragmentAdapter(TextList)



        return inflater.inflate(R.layout.fragment_navigations, container, false)

    }



    fun addDiary() {

        Log.d("debug","addDiary called")

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

