package com.mee.expensetracker.ui.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Gravity
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TimePicker
import com.mee.expensetracker.base.BaseDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DateTimeDialogFragment: BaseDialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var listener: DateTimeFragmentListener
    private var dateId: Int = 0
    private var calendar: Calendar = Calendar.getInstance()
    private val year: Int get() { return calendar.get(Calendar.YEAR) }
    private val month: Int get() { return calendar.get(Calendar.MONTH) }
    private val day: Int get() { return calendar.get(Calendar.DAY_OF_MONTH) }
    private val hour: Int get() { return calendar.get(Calendar.HOUR_OF_DAY) }
    private val minute: Int get() { return calendar.get(Calendar.MINUTE) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            if (it.containsKey(ARG_DATE))
                calendar.time  = Date(it.getLong(ARG_DATE))
            dateId = it.getInt(ARG_DATE_ID)
        }
        return DatePickerDialog(requireContext(), this,year,month,day)
    }

    private fun setTimeDialog(){
        TimePickerDialog(activity, this, hour,minute, false).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year,month,dayOfMonth)
        listener.onSetDateAndTimeResponse(dateId,calendar.time, formateDateToString())
        // setTimeDialog()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.set(year,month,day,hourOfDay,minute)
//        listener.onSetDateAndTimeResponse(dateId,calendar.time, formateDateToString())
    }

    private fun formateDateToString(): String{
        val format = SimpleDateFormat("MM/dd/yyyy")
        calendar.set(Calendar.HOUR,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        return format.format(calendar.time)
    }

    private fun setListener(listener: DateTimeFragmentListener){
        this.listener = listener
    }

    companion object{
        const val ARG_DATE = "setDate"
        const val ARG_DATE_ID = "setDateId"

        @JvmStatic
        fun newInstance(setDate: Date? = Date(), setDateId: Int = 0, listener: DateTimeFragmentListener)
                = DateTimeDialogFragment().apply {
            arguments = Bundle().apply {
                setDate?.let { putLong(ARG_DATE, it.time) }
                putInt(ARG_DATE_ID, setDateId)
            }
            setListener(listener)
        }
        @JvmStatic
        fun formateDateToString(date: Date): String{
            val format = SimpleDateFormat("MM/dd/yyyy")
            return format.format(date.time)
        }

        @JvmStatic
        fun formateDateToMonthYear(date: Date): String{
            val format = SimpleDateFormat("MMMM, yyyy")
            return format.format(date.time)
        }

        @JvmStatic
        fun formateDateToYear(date: Date): String{
            val format = SimpleDateFormat("yyyy")
            return format.format(date.time)
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setGravity(Gravity.CENTER)
    }
    interface DateTimeFragmentListener{
        fun onSetDateAndTimeResponse(dateId:Int, date: Date, dateInString: String)
    }


}