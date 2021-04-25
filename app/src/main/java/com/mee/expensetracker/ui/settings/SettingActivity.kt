package com.mee.expensetracker.ui.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseActivity
import com.mee.expensetracker.databinding.ActivitySettingBinding
import com.mee.expensetracker.databinding.ActivityStartBinding
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.ui.main.MainActivity
import com.mee.expensetracker.ui.start.SourceIncomeDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

@AndroidEntryPoint
class SettingActivity : BaseActivity(), SourceIncomeDialogFragment.SourceIncomeDialogFragmentListener {
    lateinit var binding: ActivitySettingBinding
    val viewModel: SettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        viewModel.getIncome(onSuccess = Consumer {
                populate()
        }, onProgress = Consumer {
            showProgressLoader(it)
        }, onError = Consumer {
            toast("Failed to get income.")
        })

        binding.btnEdit.setOnClickListener {
            SourceIncomeDialogFragment.newInstance(viewModel.income).show(supportFragmentManager,"SourceIncomeDialogFragment")
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.switchMyDay.isChecked = viewModel.isMyDayTurnOn()
        binding.switchMyDay.setOnCheckedChangeListener { compoundButton, check ->
            if (!compoundButton.isPressed) return@setOnCheckedChangeListener
            viewModel.saveSwitchMyDay(check)
           if (check) toast("Your My Day is turned on. Check expenses list.")
        }
    }

    private fun populate(){
        viewModel.income?.let {
            binding.txtAmount.text = ""+ it.amount
            binding.txtCategory.text = it.range.title
            binding.txtType.text = it.incomeType.title

            binding.txtInAmount.text = ""+it.amount

            viewModel.getTotalExpense(onTotal = Consumer {total ->
                binding.txtExAmount.text = ""+total
                if (total < it.amount){
                    updateProgress(total, it.amount)
                }else updateProgress(it.amount, it.amount)
            }, onError = Consumer {
                toast("Error when getting total expenses.")
            })

            binding.txtCurrent.text = viewModel.getCurrentCalculatedTitle()
        }
    }

    private fun updateProgress(progress: Double, max: Double){
        binding.progressBar.progress = progress.toInt()
        binding.progressBar.max = max.toInt()
    }

    override fun onSourceIncome(income: Income) {
        viewModel.income = income
        populate()
        viewModel.save(
            onProgress = Consumer {
                showProgressLoader(it)
            }, onSuccess = Action {
                toast("Success")
            }, onError = Action {
                to("Failed to save income.")
            })
    }

}