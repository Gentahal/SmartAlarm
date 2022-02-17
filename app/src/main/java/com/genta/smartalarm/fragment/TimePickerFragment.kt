package com.genta.smartalarm.fragment


import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var dialoListener: TimeDialoListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialoListener = context as TimeDialoListener
    }

    override fun onDetach() {
        super.onDetach()
        if (dialoListener != null) dialoListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(activity as Context, this,hourOfDay,minute,true)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int,p2: Int){
        dialoListener?.onDialogTimeSet(tag,p1, p2)
    }

    interface  TimeDialoListener {
        fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
    }

}