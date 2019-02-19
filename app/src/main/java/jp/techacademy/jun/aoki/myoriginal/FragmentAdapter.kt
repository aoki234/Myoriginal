package jp.techacademy.jun.aoki.myoriginal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FragmentAdapter(private val myDataset: ArrayList<String>,private val myDataset2: ArrayList<String>) :
    RecyclerView.Adapter<FragmentAdapter.ViewHolder>() {


    class ViewHolder(val parentView: View) : RecyclerView.ViewHolder(parentView) {
        var textView: TextView
        var textView2:TextView
        init {
            textView = parentView.findViewById(R.id.text_view)
            textView2 = parentView.findViewById(R.id.text_view2)
        }
    }

    init {
       // Log.d("debug2",myDataset.size.toString())
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        //Log.d("debug2",textView.toString())

        return ViewHolder(itemView)
    }

    // 第１引数のViewHolderはこのファイルの上のほうで作成した`class ViewHolder`です。
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = myDataset[position]
        holder.textView2.text = myDataset2[position]

        //Log.d("debug2",myDataset.size.toString())

        //Log.d("debug2",holder.textView.text.toString())
    }

    override fun getItemCount() : Int { return myDataset.size }




}
