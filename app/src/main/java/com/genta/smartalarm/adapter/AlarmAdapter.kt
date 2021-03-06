package com.genta.smartalarm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.genta.smartalarm.data.Alarm
import com.genta.smartalarm.databinding.RowItemAlarmBinding

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

     val listAlarm: ArrayList<Alarm> = arrayListOf()

    inner class MyViewHolder(val binding: RowItemAlarmBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            itemDateAlarm.text = alarm.date
            itemTimeAlarm.text = alarm.time
            itemNoteAlarm.text = alarm.message
        }
    }

    override fun getItemCount() = listAlarm.size


    // TODO 2 perbarui kode
    fun setData(list: List<Alarm>) {
        val alarmDiffUtil = AlarmDiffUtil(listAlarm,list)
        val alarmDiffUtilResult = DiffUtil.calculateDiff(alarmDiffUtil)
        listAlarm.clear()
        listAlarm.addAll(list)
        alarmDiffUtilResult.dispatchUpdatesTo(this)
    }
}