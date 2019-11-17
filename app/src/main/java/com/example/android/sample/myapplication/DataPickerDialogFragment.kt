package com.example.android.sample.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DataPickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DatePickerDialog.OnDateSetListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OndateSetListener {
        fun onDateSeleceted()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = Calendar.YEAR
        val month = Calendar.MONTH
        val day = Calendar.DAY_OF_MONTH

        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val dateString = getDateString(year, month, day)
    }

    private fun getDateString(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dataFormat = SimpleDateFormat("yyyy/MM/dd")
        return dataFormat.format(calendar.time)
    }
}