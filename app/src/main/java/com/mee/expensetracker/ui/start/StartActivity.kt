package com.mee.expensetracker.ui.start

import android.R.attr.country
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseActivity
import com.mee.expensetracker.databinding.ActivityStartBinding
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeType
import com.mee.expensetracker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer


@AndroidEntryPoint
class StartActivity : BaseActivity(), SourceIncomeDialogFragment.SourceIncomeDialogFragmentListener {
    lateinit var binding: ActivityStartBinding
    val viewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        binding.editIncome.setOnClickListener {
            SourceIncomeDialogFragment.newInstance().show(supportFragmentManager, "SourceIncomeDialogFragment")
        }

        binding.btnStart.setOnClickListener {
            viewModel.save(
                onProgress = Consumer {
                    showProgressLoader(it)
                }, onSuccess = Action {
                    toast("Success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }, onError = Action {
                    to("Failed to save income.")
                })
        }
    }

    override fun onSourceIncome(income: Income) {
        viewModel.income = income
        binding.editIncome.setText(income.incomeType.title +" - "+income.amount+ " - "+income.range.title)
    }


}