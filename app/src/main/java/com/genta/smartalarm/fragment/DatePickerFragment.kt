package com.genta.smartalarm.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment :DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dialogListener: DateDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DateDialogListener
    }
    //mengatipasi Bug pake onDetach
    override fun onDetach() {
        super.onDetach()
        if (dialogListener != null) dialogListener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity as Context,this,year,month,day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dialogListener?.onDialogDateSet(tag,year,month,dayOfMonth)
        Log.i(tag,"onDataSet: $year, $month, $dayOfMonth")
    }

    // buat dipanggil di activity supaya dapat nilai inputan yang sudah dipilih
    interface DateDialogListener {
        fun onDialogDateSet(tag: String?, year: Int,month: Int,dayOfMonth: Int)
    }
}