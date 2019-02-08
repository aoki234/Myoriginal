package jp.techacademy.jun.aoki.myoriginal

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class NavigationFragment : Fragment() {

    var TextList: ArrayList<String> = ArrayList()
    var TextList2: ArrayList<String> = ArrayList()
    private var mRecyclerView: RecyclerView? = null
    private var mDirectionButton: Button? = null
    private var mTimerButton: Button? = null
    //var bus: List<Bus>? = null
    var gson: Gson? = null
    var json: String? = null


    var hourArray: ArrayList<String> = ArrayList()
    var minuteArray: ArrayList<String> = ArrayList()

    var position: Int = 0
    val c = Calendar.getInstance()
    val currenthour = c.get(Calendar.HOUR_OF_DAY)
    val currentminute = c.get(Calendar.MINUTE)

    var busArrayList: ArrayList<Bus> = ArrayList<Bus>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val v = inflater.inflate(R.layout.fragment_navigations, container, false)

        mRecyclerView = v.findViewById(R.id.recycler_view)


        getBusdatas()

        //Log.d("debug_data5",busArrayList.toString())
        //バスの発車口、時刻をRecycterViewで表示する
        //addDiary()
        //addDiary2()


        mDirectionButton = v.findViewById<Button>(R.id.button1)
        mDirectionButton!!.setOnClickListener {
            changeDdirection()
        }

        mTimerButton = v.findViewById<Button>(R.id.button2)
        mTimerButton!!.setOnClickListener {
            showTimePickerDialog()
        }



        return v //inflater.inflate(R.layout.fragment_navigations, container, false)

    }


    fun addDiary() {

        Log.d("debug", "addDiary called")
        //Log.d("debug_data",busArrayList.direction.toString())
        // Log.d("debug_data",busArrayList.get(0).toString())
        //busparser()
        //for (i in 0 until bus!!.count()) {
        //   TextList.add(bus!!.get(i).direction)
        // }

        for (count in busArrayList) {
            Log.d("debug_data2", count.direction.toString())
            //Log.d("debug12",direction.toString())
            TextList.add(count.direction)
        }

    }

    fun addDiary2() {

        Log.d("debug", "addDiary2 called")
        //busparser()

        for (count in busArrayList) {

            Log.d("debug_data3", count.time.toString())
            TextList2.add(count.time)

            var time = count.time.split(Regex("[^0-9]"))

            hourArray.add(time[0])
            minuteArray.add(time[1])

        }
    }

    //jsonファイルを取得
    fun readJSONFromAsset(): String? {
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
    }


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
        } else {
            mDirectionButton!!.setText("キャンパス発")
        }
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
        for (x in hourArray.indices) {
            //設定した時刻を表示するための処理
            Log.d("debug6", x.toString())

            if (hour <= hourArray[x].toInt()) {

                Log.d("debug17", minuteArray[x])

                if (minute <= minuteArray[x].toInt()) {
                    Log.d("debug7", minuteArray[x])
                    //表示する時刻表のセルを設定
                    position = x
                    break
                }
            } else {

                //当日のバスが存在しない場合
                Log.d("debug7", "time else called")
                position = 0
            }
        }

        (mRecyclerView!!.layoutManager as LinearLayoutManager).scrollToPosition(position)

        position =
                (mRecyclerView!!.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()// ポジション保存
    }

    fun getBusdatas(){
        //バスのデータをサーバから取得する
        val mDataBaseReference = FirebaseDatabase.getInstance().reference
        var count = 0

        mDataBaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {

                    val map = dataSnapshot.value as Map<String, String>

                    val direction = map["direction"] ?: ""
                    val time = map["time"] ?: ""

                if (direction != null) {

                    val bus = Bus(direction, time)
                    //Log.d("debug_data",bus.toString())

                    busArrayList.add(bus)

                    count += 1
                    Log.d("debug_data", count.toString())

                }else{

                    addDiary()
                    addDiary2()

                    mRecyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    mRecyclerView!!.adapter = FragmentAdapter(TextList, TextList2)

                    //最も現在時刻に近いバスの時刻を表示する
                    changeTimeCell(currenthour, currentminute)

                }


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

}



