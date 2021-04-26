package com.mee.expensetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.carded.api.ExpenseContent
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseFragment
import com.mee.expensetracker.databinding.FragmentMainBinding
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.ui.expense.ExpenseAdapter
import com.mee.expensetracker.ui.expense.SaveExpenseDialogFragment
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_my_day.*


class MainFragment : BaseFragment(), ExpenseAdapter.Callback, SaveExpenseDialogFragment.SaveExpenseDialogFragmentListener {
    lateinit var binding: FragmentMainBinding
    val viewModel: MainViewModel by activityViewModels()
    var adapter: ExpenseAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(mutableListOf(), this)
        binding.recyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            SaveExpenseDialogFragment.newInstance(listener = this).show(childFragmentManager,"SaveExpenseDialogFragment")
        }

        binding.btnSetting.setOnClickListener {
            val nav = Navigation.findNavController(requireView())
            nav.navigate(R.id.action_mainFragment_to_settingActivity)
        }

        getAll()
        viewModel.expenseContents.observe(viewLifecycleOwner, Observer {
            adapter?.list = it.toMutableList()
            adapter?.notifyDataSetChanged()
        })

        layoutMyDayMain.setOnClickListener {
            viewModel.parseExpenseContentForMyDay(Consumer {
                txt_Total.text = ""+it
            })
        }
        binding.btnRefresh.setOnClickListener {
            getAll()
        }
    }
    override fun onResume() {
        super.onResume()
        layoutMyDayMain.visibility  = if (viewModel.isMyDayTurnOn()) View.VISIBLE else View.GONE

    }

    fun getAll(){
        viewModel.getAll(Consumer {
            toast("Failed to get expenses list.")
        })
    }

    override fun onSaveExpense(expense: Expense) {
        viewModel.expense = expense
        viewModel.save(onSuccess = Action {
            toast("Expense saved.")
            getAll()
        }, onError = Action {
            toast("Failed to save expense")
        }, onProgress = Consumer {
            showProgressLoader(it)
        })
    }

    override fun onSelectedExpense(expenseContent: ExpenseContent.Item) {
        SaveExpenseDialogFragment.newInstance(expenseContent.expense, this).show(childFragmentManager,"SaveExpenseDialogFragment")

    }
}