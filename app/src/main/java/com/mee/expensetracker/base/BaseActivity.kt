package com.mee.expensetracker.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mee.expensetracker.ui.progress.ProgressDialog

/**
 * Created by Michelle Dayangco
 */
open class BaseActivity: AppCompatActivity() {
    var navigator: Navigator? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)

    }

    override fun onBackPressed() {
        navigator?.let {navigator ->
            if(navigator.isBackStackEmpty()){
                super.onBackPressed()
            } else { navigator.previous() }
        }?:run { super.onBackPressed() }
    }

    fun toast(message:String){
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }

    private fun showProgressLoader() {
        if (!isFinishing) progressDialog!!.showDialog(ProgressDialog.DIALOG_CENTERED)
    }


    private fun dismissProgressLoader() {
        if (!isFinishing) progressDialog!!.dismiss()
    }

    fun showProgressLoader(show: Boolean){
        if (show) showProgressLoader()
        else dismissProgressLoader()
    }
}