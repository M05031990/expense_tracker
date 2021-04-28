package com.mee.expensetracker.ui.expense

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.carded.api.CategoryContent
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseDialogFragment
import com.mee.expensetracker.databinding.FragmentInputExpensesBinding
import com.mee.expensetracker.databinding.FragmentInputIncomeBinding
import com.mee.expensetracker.model.*
import com.mee.expensetracker.ui.category.CategoryAdapter
import com.mee.expensetracker.ui.datepicker.DateTimeDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.*

/**
 * Created by Michelle Dayangco
 */
@AndroidEntryPoint
class SaveExpenseDialogFragment: BaseDialogFragment() {
    lateinit var binding: FragmentInputExpensesBinding
    val viewModel: ExpenseViewModel by activityViewModels()
    var listener: SaveExpenseDialogFragmentListener? = null
    var adapter: CategoryAdapter? = null
    var expense: Expense? = null
    var setDate: Date? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_input_expenses, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)

        populate()
        updateAdapter()

        setDate = if (expense != null) expense!!.create_at else Date()
        binding.editDate.setOnClickListener {
            DateTimeDialogFragment.newInstance(setDate,0, object : DateTimeDialogFragment.DateTimeFragmentListener{
                override fun onSetDateAndTimeResponse(
                    dateId: Int,
                    date: Date,
                    dateInString: String
                ) {
                    setDate = date
                    binding.editDate.setText(dateInString)
                }
            }).show(childFragmentManager,"DateTimeDialogFragment")
        }

        binding.btnDelete.visibility = if (expense != null) View.VISIBLE else View.GONE
        binding.btnDelete.setOnClickListener {
            viewModel.delete(expense!!, Action {
                toast("deleted")
                listener?.onUpdateExpenses()
                dismiss()
            })
        }
        binding.btnSave.setOnClickListener {
            if (validate()){
               listener?.onSaveExpense(getUpdatedExpense())
                dismiss()
            }
            else toast(getString(R.string.required_field_is_empty))
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
    private fun validate(): Boolean{
        if (binding.editAmount.text.toString().isNullOrEmpty() ||
                binding.editTitle.text.toString().isNullOrEmpty() ||
                binding.editDate.text.toString().isNullOrEmpty() ||
                binding.editDesc.text.toString().isNullOrEmpty()) {
            toast(getString(R.string.required_field_is_empty))
            return false
        }
        return true
    }

    private fun populate(){
        expense?.let {
            binding.editTitle.setText(it.title)
            binding.editAmount.setText(""+it.amount)
            binding.editDate.setText(DateTimeDialogFragment.formateDateToString(it.create_at))
            binding.editDesc.setText(it.description)
            binding.checkBoxMyDay.isChecked = it.isMyDay?:false
        }
    }

    private fun updateAdapter(){

        val list: MutableList<CategoryContent> = mutableListOf()
         expense?.let {
             it.categories?.forEach {
                 list.add(CategoryContent.Item(it))
             }
         }
        list.add(CategoryContent.Add())
        adapter = CategoryAdapter(list)
        binding.recyclerView.adapter = adapter
    }

    private fun getUpdatedExpense(): Expense {
        var uExpense: Expense = Expense()
        if (expense != null) uExpense = expense!!

        uExpense.title = binding.editTitle.text.toString()
        uExpense.amount = binding.editAmount.text.toString().toDouble()
        uExpense.create_at = setDate?: Date()
        uExpense.categories = getCategoryItems()
        uExpense.description = binding.editDesc.text.toString()
        uExpense.isMyDay = binding.checkBoxMyDay.isChecked

        return uExpense
    }

    private fun getCategoryItems(): List<String>{
        val list: MutableList<String> = mutableListOf()
        adapter?.list?.forEach {
            if (it is CategoryContent.Item)
                list.add(it.content)
        }
        return list
    }

    interface SaveExpenseDialogFragmentListener{
        fun onSaveExpense(expense: Expense)
        fun onUpdateExpenses()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is SaveExpenseDialogFragmentListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement SaveExpenseDialogFragmentListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    companion object{

        @JvmStatic
        fun newInstance(expense: Expense? = null, listener: SaveExpenseDialogFragmentListener) = SaveExpenseDialogFragment().apply {
            this.expense  = expense
            this.listener = listener
        }
    }


}