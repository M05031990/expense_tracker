package com.mee.expensetracker.base

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mee.expensetracker.ui.progress.ProgressDialog


/**
 * Created by Michelle Dayangco
 */
open class BaseDialogFragment: DialogFragment() {
    private var progressDialog: ProgressDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())

    }
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

    private fun showProgressLoader() {
        progressDialog!!.showDialog(ProgressDialog.DIALOG_CENTERED)
    }


    private fun dismissProgressLoader() {
        progressDialog!!.dismiss()
    }

    fun showProgressLoader(show: Boolean){
        if (show) showProgressLoader()
        else dismissProgressLoader()
    }
}