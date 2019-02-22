package jp.techacademy.jun.aoki.myoriginal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

class HomeFragment : Fragment() {


    val gridlist = ArrayList<String>()
    val members = arrayOf("yumiko", "yuka", "mai", "nagi", "toko", "kurumi", "miki", "saya", "yuyu", "katakuriko")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_home, container, false)

        val adapter = HomeAdapter()

        val gridview = v.findViewById<GridView>(R.id.gridview)
        // Set the grid view adapter
        gridview!!.adapter = adapter

        // Configure the grid view
        gridview.numColumns = 5
        gridview.horizontalSpacing = 2
        gridview.verticalSpacing = 2

        gridview.stretchMode = GridView.STRETCH_COLUMN_WIDTH


        return v
    }
}

