package com.mee.expensetracker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseFragment
import com.mee.expensetracker.databinding.FragmentSettingBinding
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.ui.income.SourceIncomeDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Consumer

@AndroidEntryPoint
class SettingFragment : BaseFragment(), SourceIncomeDialogFragment.SourceIncomeDialogFragmentListener {
    lateinit var binding: FragmentSettingBinding
    val viewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIncome(onSuccess = Consumer {
            populate()
        }, onProgress = Consumer {
            showProgressLoader(it)
        }, onError = Consumer {
            toast("Failed to get income.")
        })

        binding.btnEdit.setOnClickListener {
            SourceIncomeDialogFragment.newInstance(viewModel.income,this).show(childFragmentManager,"SourceIncomeDialogFragment")
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
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
    }

}