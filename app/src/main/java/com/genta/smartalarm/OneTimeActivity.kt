package com.genta.smartalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.genta.smartalarm.data.Alarm
import com.genta.smartalarm.data.local.AlarmDB
import com.genta.smartalarm.data.local.AlarmDao
import com.genta.smartalarm.databinding.ActivityOneTimeBinding
import com.genta.smartalarm.fragment.DatePickerFragment
import com.genta.smartalarm.fragment.TimePickerFragment
import com.genta.smartalarm.helper.TAG_TIME_PICKER
import com.genta.smartalarm.helper.timeFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTimeActivity : AppCompatActivity(), DatePickerFragment.DateDialogListener,
    TimePickerFragment.TimeDialoListener {

    private var _binding: ActivityOneTimeBinding? = null
    private val binding get() = _binding as ActivityOneTimeBinding

    private var alarmDao: AlarmDao? = null

    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService

// private val db by lazy { AlarmDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOneTimeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val db = AlarmDB.getDataBase(applicationContext)
        alarmDao = db.alaramDao()

        _alarmService = AlarmService()

        initView()
    }

    private fun initView() {
        binding.apply {

            btnSetOneDate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "DatePickerDialog")
            }

            btnSetTimeOneTime.setOnClickListener {
                val datePickerFragment = TimePickerFragment()
                datePickerFragment.show(supportFragmentManager, TAG_TIME_PICKER)
            }

            btnAddSetOneTime.setOnClickListener {
                val date = tvOneDate.text.toString()
                val time = tvOneTime.text.toString()
                val note = edtNoteOneTime.text.toString()

                if (date != "Date" && time != "Time") {
                    alarmService.setOneTimeAlarm(applicationContext, 1, date, time, note)
                    CoroutineScope(Dispatchers.IO).launch {
                        alarmDao?.addAlarm(
                            Alarm(
                                0,
                                date,
                                time,
                                note,
                                AlarmService.TYPE_ONE_TIME
                            )
                        )
                        Log.i("AddAlarm", "Succes set alarm on $date $time with message")
                        finish()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You must set the alarm!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnCancelSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()

//  mengatur tanggal supaya sama dengan yang sudah di pilih di dialog picker
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.tvOneDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvOneTime.text = timeFormatter(hourOfDay, minute)
    }
}