package com.mee.expensetracker.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseDialogFragment
import com.mee.expensetracker.databinding.FragmentInputIncomeBinding
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeTimeRange
import com.mee.expensetracker.model.IncomeType

/**
 * Created by Michelle Dayangco
 */
class SourceIncomeDialogFragment: BaseDialogFragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentInputIncomeBinding
    var listener: SourceIncomeDialogFragmentListener? = null
    var income: Income? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_input_income, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val incomeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,
            IncomeType.getList())
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIncome.setAdapter(incomeAdapter)
        binding.spinnerIncome.setOnItemSelectedListener(this)

        val rangeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,
            IncomeTimeRange.getList())
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRange.setAdapter(rangeAdapter)
        binding.spinnerRange.setOnItemSelectedListener(this)

        populate()
        binding.btnSetIncome.setOnClickListener {
            if (validate()){
                listener?.onSourceIncome(getUpdateIncome())
                dismiss()
            }
            else toast(getString(R.string.required_field_is_empty))
        }
    }
    private fun populate(){
        income?.let {
            binding.spinnerIncome.setSelection(it.incomeType.ordinal)
            binding.spinnerRange.setSelection(it.range.ordinal)
            binding.editAmount.setText(""+it.amount)
        }

    }
    private fun validate(): Boolean{
        if (binding.editAmount.text.toString().isNullOrEmpty())
            return false
        return true
    }
    private fun getSelectedIncomeType(): IncomeType{
        val str = binding.spinnerIncome.selectedItem as String
        return IncomeType.Companion.getIncomeType(str)
    }

    private fun getSelectedRangeType(): IncomeTimeRange{
        val str = binding.spinnerRange.selectedItem as String
        return IncomeTimeRange.getType(str)
    }
    private fun getUpdateIncome(): Income{
        var uIncome = Income()
        if (income != null) uIncome = income!!

        uIncome. incomeType = getSelectedIncomeType()
        uIncome.amount = binding.editAmount.text.toString().toDouble()
        uIncome.range = getSelectedRangeType()

        return uIncome
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }
    interface SourceIncomeDialogFragmentListener{
        fun onSourceIncome(income: Income)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SourceIncomeDialogFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement SourceIncomeDialogFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    companion object{

        @JvmStatic
        fun newInstance(income: Income? = null) = SourceIncomeDialogFragment().apply {
            this.income = income
        }
    }


}