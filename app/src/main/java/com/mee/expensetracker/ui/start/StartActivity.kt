package com.mee.expensetracker.ui.start

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseActivity
import com.mee.expensetracker.databinding.ActivityStartBinding
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.ui.income.SourceIncomeDialogFragment
import com.mee.expensetracker.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer


@AndroidEntryPoint
class StartActivity : BaseActivity(), SourceIncomeDialogFragment.SourceIncomeDialogFragmentListener {
    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)

        binding.editIncome.setOnClickListener {
            SourceIncomeDialogFragment.newInstance(listener = this).show(supportFragmentManager, "SourceIncomeDialogFragment")
        }

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onSourceIncome(income: Income) {
        binding.editIncome.setText( String.format("%s - %s - %s", income.incomeType.title,income.amount,income.range.title))
    }


}