package com.mee.expensetracker.base

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment


/**
 * Created by Michelle Dayangco
 */
open class BaseDialogFragment: DialogFragment() {

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            dialog?.getWindow()
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun toast(message:String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
    }
}