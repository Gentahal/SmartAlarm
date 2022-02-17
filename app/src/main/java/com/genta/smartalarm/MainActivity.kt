package com.genta.smartalarm

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genta.smartalarm.adapter.AlarmAdapter
import com.genta.smartalarm.data.Alarm
import com.genta.smartalarm.data.local.AlarmDB
import com.genta.smartalarm.data.local.AlarmDao
import com.genta.smartalarm.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding as ActivityMainBinding

    private var alarmAdapter : AlarmAdapter? = null

    private var alarmDao : AlarmDao? = null

    private var alarmService : AlarmService? = null

    override fun onResume() {
        super.onResume()
        alarmDao?.getAlarm()?.observe(this) {
            alarmAdapter?.setData(it)
            Log.i("GetAlarm", "getAlarm : alarm with $it")
        }
    }
//        CoroutineScope(Dispatchers.IO).launch {
//            val alarm = alarmDao?.getAlarm()
//            withContext(Dispatchers.Main) {
//                alarm?.let { alarmAdapter?.setData(it) }
//            }
//            Log.i("GetAlarm","getAlarm : alarm with $alarm")
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDataBase(applicationContext)
        alarmDao = db.alaramDao()

        alarmService = AlarmService()

        initview()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvReminderAlarm.apply {
            alarmAdapter = AlarmAdapter()
            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
            swipeToDelete(this)
        }
    }

    private fun initview(){
        binding.apply {
            //@ bisa juga di ganti dengan applicationContext
           cvSetOneTime.setOnClickListener {
               startActivity(Intent(this@MainActivity,OneTimeActivity::class.java))
           }

            cvSetRepeatingTime.setOnClickListener {
                startActivity(Intent(this@MainActivity,RepeatingAlarm::class.java))
            }
        }
        getTimeToday()
    }
    private fun getTimeToday(){

        binding.tvTimeToday.format24Hour
        binding.tvTimeToday.format24Hour
    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deleteAlarm?.let { alarmDao?.deleteAlarm(it) }
                    Log.i("DeleteAlarm","onSwiped : deleteAlarm $deleteAlarm")
                }
                val alarmType = deleteAlarm?.type
                alarmType?.let { alarmService?.cancelAlarm(baseContext, it) }

            }

        }).attachToRecyclerView(recyclerView)
    }
}