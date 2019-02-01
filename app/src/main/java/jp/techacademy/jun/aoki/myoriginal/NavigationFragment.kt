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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.util.*


class NavigationFragment : Fragment() {

    var TextList: ArrayList<String> = ArrayList()
    var TextList2: ArrayList<String> = ArrayList()
    private var mRecyclerView: RecyclerView? = null
    private var mDirectionButton: Button? = null
    private var mTimerButton:Button? = null
    var bus : List<Bus>? =null
    var gson:Gson? = null
    var json: String? = null


    var hourArray: ArrayList<String> = ArrayList()
    var minuteArray: ArrayList<String> = ArrayList()

    var position: Int = 0
    val c = Calendar.getInstance()
    val currenthour = c.get(Calendar.HOUR_OF_DAY)
    val currentminute = c.get(Calendar.MINUTE)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        val v = inflater.inflate(R.layout.fragment_navigations, container, false)

        mRecyclerView = v.findViewById(R.id.recycler_view)

        //バスの発車口、時刻をRecycterViewで表示する
        addDiary()
        addDiary2()


        mRecyclerView!!.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)


        Log.d("debug",mRecyclerView!!.layoutManager.toString())

        mRecyclerView!!.adapter = FragmentAdapter(TextList,TextList2)

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

        Log.d("debug","addDiary called")
        busparser()
        for (i in 0 until bus!!.count()) {
            TextList.add(bus!!.get(i).direction)
        }

    }

    fun addDiary2() {

        Log.d("debug","addDiary2 called")
        busparser()

        for (i in 0 until bus!!.count()) {
            TextList2.add(bus!!.get(i).time)

            var time = bus!!.get(i).time.split(Regex("[^0-9]"))

            Log.d("debug4",time[1])


            hourArray.add(time[0])
            minuteArray.add(time[1])

        }
    }

    //jsonファイルを取得
    fun readJSONFromAsset(): String? {
        try {
            val  inputStream: InputStream = context!!.getAssets().open("test.json")
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
    fun busparser(): List<Bus>? {

        gson = Gson()
        //val message = context!!.assets.open("test.json").reader(charset=Charsets.UTF_8)
        var message = readJSONFromAsset()
        val listType = object : TypeToken<List<Bus>>() {}.type
        bus = gson!!.fromJson(message, listType)

        return bus
    }

    //行き先を指定する
    fun changeDdirection(){
        if(mDirectionButton!!.getText().toString() == "キャンパス発") {

            mDirectionButton!!.setText("小手指駅発")
        }else{
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
            changeTimeCell(hour,minute)
            },
            currenthour, currentminute, false)
        timePickerDialog.show()

    }


    fun changeTimeCell(hour:Int,minute:Int){
        for(x in hourArray.indices){
            //設定した時刻を表示するための処理
            Log.d("debug6",x.toString())

            if(hour <= hourArray[x].toInt()){

                if(minute <= minuteArray[x].toInt()){
                    Log.d("debug7",minuteArray[x])
                    //表示する時刻表のセルを設定
                    position = x
                    break
                }
            }
        }

        (mRecyclerView!!.layoutManager as LinearLayoutManager).scrollToPosition(position)

        position = (mRecyclerView!!.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()// ポジション保存
    }


    override fun onResume() {
        super.onResume()

        changeTimeCell(currenthour,currentminute)
    }

}




