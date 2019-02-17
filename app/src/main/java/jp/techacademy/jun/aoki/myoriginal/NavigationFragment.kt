package jp.techacademy.jun.aoki.myoriginal

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class NavigationFragment : Fragment() {


    private var mRecyclerView: RecyclerView? = null
    private var mDirectionButton: Button? = null
    private var mTimerButton: Button? = null
    //var bus: List<Bus>? = null
    //var gson: Gson? = null
    //var json: String? = null
    var hourArray: ArrayList<String> = ArrayList()
    var minuteArray: ArrayList<String> = ArrayList()

    var position: Int = 0
    val c = Calendar.getInstance()
    val currenthour = c.get(Calendar.HOUR_OF_DAY)
    val currentminute = c.get(Calendar.MINUTE)

    private var mDeparture:String = "campus"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_navigations, container, false)

        mRecyclerView = v.findViewById(R.id.recycler_view)


        getBusdatas(mDeparture)

        //Log.d("debug_data5",busArrayList.toString())
        //バスの発車口、時刻をRecycterViewで表示する
        //addDiary()
        //addDiary2()


        mDirectionButton = v.findViewById(R.id.button1)
        mDirectionButton!!.setOnClickListener {
            changeDdirection()
        }

        mTimerButton = v.findViewById<Button>(R.id.button2)
        mTimerButton!!.setOnClickListener {
            showTimePickerDialog()
        }



        return v //inflater.inflate(R.layout.fragment_navigations, container, false)

    }


    fun addDiary(busArrayList: ArrayList<Bus>): ArrayList<String> {

        var TextList: ArrayList<String> = ArrayList()
        Log.d("debug", "addDiary called")

        for (count in busArrayList) {
           
            TextList.add(count.direction)
        }
        return TextList
    }

    fun addDiary2(busArrayList: ArrayList<Bus>): ArrayList<String> {

        var TextList2: ArrayList<String> = ArrayList()
        Log.d("debug", "addDiary2 called")
        //busparser()Log.d("debug_data3", count.time.toString())
        Log.d("debug_data3", busArrayList.toString())

        for (count in busArrayList) {

            Log.d("debug_data3", count.time.toString())
            TextList2.add(count.time)

            var time = count.time.split(Regex("[^0-9]"))

            hourArray.add(time[0])
            minuteArray.add(time[1])

        }
        return TextList2
    }

    //jsonファイルを取得
    /*fun readJSONFromAsset(): String? {
        try {
            val inputStream: InputStream = context!!.getAssets().open("test.json")
            //json = inputStream.bufferedReader().use{it.readText()}
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)

        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }*/


    //jsonから値を取得する
    /*fun busparser(): List<Bus>? {

        gson = Gson()
        //val message = context!!.assets.open("test.json").reader(charset=Charsets.UTF_8)
        var message = readJSONFromAsset()
        val listType = object : TypeToken<List<Bus>>() {}.type
        bus = gson!!.fromJson(message, listType)

        return bus
    }*/

    //行き先を指定する
    fun changeDdirection() {
        if (mDirectionButton!!.getText().toString() == "キャンパス発") {

            mDirectionButton!!.setText("小手指駅発")

            mDeparture = "kotesashi"




        } else {
            mDirectionButton!!.setText("キャンパス発")

            mDeparture = "campus"
        }
        getBusdatas(mDeparture)

    }

    //表示する時間を設定する
    private fun showTimePickerDialog() {

        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { activity, hour, minute ->
                Log.d("UI_PARTS", "$hour:$minute")

                //設定した時刻を反映する
                changeTimeCell(hour, minute)
            },
            currenthour, currentminute, false
        )
        timePickerDialog.show()

    }


    fun changeTimeCell(hour: Int, minute: Int) {

        if(hourArray != null && minuteArray != null) {
            for (x in hourArray.indices) {
                //設定した時刻を表示するための処理
                //Log.d("debug6", x.toString())

                //バスは必ず8時以降の発車となるため、早朝の時刻設定に対応した
                if (hour >= 7 && hour <= hourArray[x].toInt()) {

                    Log.d("debug17", hourArray[x])

                    if (minute <= minuteArray[x].toInt()) {
                        Log.d("debug7", minuteArray[x])
                        //表示する時刻表のセルを設定
                        position = x
                        Log.d("debug_position",position.toString())
                        break
                    }
                } else {
                    //当日のバスが存在しない場合
                    //Log.d("debug7", "time else called")
                    position = 0
                }
            }


        (mRecyclerView!!.layoutManager as LinearLayoutManager).scrollToPosition(position)

        position =
                (mRecyclerView!!.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()// ポジション保存

        }else{
            //サーバからのデータの取得に失敗した場合
            Snackbar.make(getView()!!,"データが存在しません",Snackbar.LENGTH_LONG).show()
        }
    }

    fun getBusdatas(departure:String){


        //バスのデータをサーバから取得する
        val mDataBaseReference = FirebaseDatabase.getInstance().reference.child(departure)

            mDataBaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot){

                    val map = dataSnapshot!!.children
                    var busArrayList: ArrayList<Bus> = ArrayList<Bus>()

                    map.forEach {

                        var direction = it.child("direction").value as String
                        var time = it.child("time").value as String

                        Log.d("firebase", time.toString())
                        val bus = Bus(direction, time)
                        busArrayList.add(bus)
                    }


                    Log.d("debug", "called from here")
                    mRecyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    mRecyclerView!!.adapter = FragmentAdapter(addDiary(busArrayList), addDiary2(busArrayList))

                    //最も現在時刻に近いバスの時刻を表示する
                    changeTimeCell(currenthour, currentminute)

                }

                override fun onCancelled(databaseError: DatabaseError){

                }

            })

    }

}



