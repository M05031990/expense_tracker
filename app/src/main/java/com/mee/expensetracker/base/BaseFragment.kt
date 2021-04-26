package com.mee.expensetracker.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mee.expensetracker.ui.progress.ProgressDialog

/**
 * Created by Michelle Dayangco
 */
open class BaseFragment: Fragment() {
    private var progressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())

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