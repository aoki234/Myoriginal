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
    var TextList2: ArrayList<String> = ArrayList()
    private var mRecyclerView: RecyclerView? = null
    private var mDirectionButton: Button? = null
    //var array:Array<Array<String>> = arrayOf(3, arrayOf(("北口","08時00分00"),("北口","08時04分00"),("北口","08時08分00")))

    //var BusList = listOf<Array<String>>(("北口","08時00分00"),("北口","08時04分00"))

    //array = (("北口","08時00分00"),("北口","08時04分00"),("北口","08時08分00"))


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //var array:Array<Array<String> = arrayOf(("北口","08時00分00"),("北口","08時04分00"),("北口","08時08分00"))

        val v = inflater.inflate(R.layout.fragment_navigations, container, false)

        mRecyclerView = v.findViewById(R.id.recycler_view)

        addDiary()
        addDiary2()

        Log.d("debug",TextList.toString())

        mRecyclerView!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)


        Log.d("deug",mRecyclerView!!.layoutManager.toString())

        mRecyclerView!!.adapter = FragmentAdapter(TextList,TextList2)



        //button
        mDirectionButton = v.findViewById(R.id.button1)

        mDirectionButton!!.setOnClickListener {
            if(mDirectionButton!!.getText().toString() == "キャンパス発") {

                mDirectionButton!!.setText("小手指駅発")
            }else{
                mDirectionButton!!.setText("キャンパス発")
            }
        }



        return v //inflater.inflate(R.layout.fragment_navigations, container, false)

    }



        fun addDiary() {

        Log.d("debug","addDiary called")

        TextList.add("1")
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

    fun addDiary2() {

        Log.d("debug","addDiary called")

        TextList2.add("1")
        TextList2.add("diary2")
        TextList2.add("diary3")
        TextList2.add("diary4")
        TextList2.add("diary5")
        TextList2.add("diary6")
        TextList2.add("diary7")
        TextList2.add("diary8")
        TextList2.add("diary10")
        TextList2.add("diary11")
        TextList2.add("diary12")
        TextList2.add("diary13")
    }
}

