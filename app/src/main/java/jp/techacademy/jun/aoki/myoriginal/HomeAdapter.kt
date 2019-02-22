package jp.techacademy.jun.aoki.myoriginal

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast


class HomeAdapter: BaseAdapter(){

    private var list = colors()

    override fun getView(position:Int, convertView: View?, parent: ViewGroup?):View{
        // Inflate the custom view
        val inflater = parent?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.gridcell_layout,null)

        // Get the custom view widgets reference
        val tv = view.findViewById<TextView>(R.id.tv_name)
        val card = view.findViewById<CardView>(R.id.card_view)

        // Display color name on text view
        tv.text = list[position].first

        // Set background color for card view
        card.setCardBackgroundColor(list[position].second)

        // Set a click listener for card view
        card.setOnClickListener{
            // Show selected color in a toast message
            Toast.makeText(parent.context,
                "Clicked : ${list[position].first}",Toast.LENGTH_SHORT).show()

            // Get the activity reference from parent
            val activity  = parent.context as Activity

            // Get the activity root view
            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0)

            // Change the root layout background color
            viewGroup.setBackgroundColor(list[position].second)
        }

        // Finally, return the view
        return view
    }



    /*
        **** reference source developer.android.com ***

        Object getItem (int position)
            Get the data item associated with the specified position in the data set.

        Parameters
            position int : Position of the item whose data we want within the adapter's data set.
        Returns
            Object : The data at the specified position.
    */
    override fun getItem(position: Int): Any? {
        return list[position]
    }



    /*
        **** reference source developer.android.com ***

        long getItemId (int position)
            Get the row id associated with the specified position in the list.

        Parameters
            position int : The position of the item within the adapter's data
                           set whose row id we want.
        Returns
            long : The id of the item at the specified position.
    */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    // Count the items
    override fun getCount(): Int {
        return list.size
    }




    // Custom method to generate list of color name value pair
    private fun colors():List<Pair<String,Int>>{
        return listOf(
            Pair("CRIM",Color.parseColor("#FFFFFF")),
            Pair("RED",Color.parseColor("#FFFFFF")),
            Pair("FIREB",Color.parseColor("#FFFFFF")),
            Pair("DARKE",Color.parseColor("#FFFFFF")),
            Pair("PINK",Color.parseColor("#FFFFFF")),

            Pair("LIGHTPI",Color.parseColor("#FFFFFF")),
            Pair("HOTPI",Color.parseColor("#FFFFFF")),
            Pair("DEEPPI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUM",Color.parseColor("#FFFFFF")),
            Pair("PALEVI",Color.parseColor("#FFFFFF")),

            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),

            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),

            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF")),
            Pair("MEDIUMVI",Color.parseColor("#FFFFFF"))



        )
    }
}