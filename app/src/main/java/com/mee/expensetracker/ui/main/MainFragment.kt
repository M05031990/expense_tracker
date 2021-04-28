package com.mee.expensetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.carded.api.CategoryContent
import com.carded.api.ExpenseContent
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseFragment
import com.mee.expensetracker.databinding.FragmentMainBinding
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.FilterDateRange
import com.mee.expensetracker.model.IncomeTimeRange
import com.mee.expensetracker.ui.category.CategoryAdapter
import com.mee.expensetracker.ui.expense.ExpenseAdapter
import com.mee.expensetracker.ui.expense.SaveExpenseDialogFragment
import com.mee.expensetracker.util.AppUtils
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_filter.view.*
import kotlinx.android.synthetic.main.layout_my_day.*


class MainFragment : BaseFragment(), ExpenseAdapter.Callback, SaveExpenseDialogFragment.SaveExpenseDialogFragmentListener {
    lateinit var binding: FragmentMainBinding
    val viewModel: MainViewModel by activityViewModels()
    var adapter: ExpenseAdapter? = null
    var container: ViewGroup? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        this.container = container
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

            if (viewModel.isMyDayTurnOn()){
                viewModel.getTotalExForMyDay(Consumer {
                    txt_Total.text = ""+it
                })
            }
        })
        layoutMyDayMain.setOnClickListener {
           populateMyDay()
        }
        binding.btnRefresh.setOnClickListener {
            getAll()
        }

        binding.btnFilter.setOnClickListener {
            popFilterWindow()
        }
    }
    override fun onResume() {
        super.onResume()
        layoutMyDayMain.visibility  = if (viewModel.isMyDayTurnOn()){
            View.VISIBLE
        } else View.GONE

    }

    fun getAll(){
        viewModel.getAll(Consumer {
            toast("Failed to get expenses list.")
        })
    }

    fun populateMyDay(){
        viewModel.parseExpenseContentForMyDay(Consumer {
            txt_Total.text = ""+it
        })
    }

    fun popFilterWindow(){
        val view =  LayoutInflater.from(requireContext()).inflate(R.layout.layout_filter,container,false)
        val popupWindow = PopupWindow(view,AppUtils.getScreenWidth(requireActivity()),AppUtils.getScreenWidth(requireActivity()))

        val list: MutableList<CategoryContent> = mutableListOf()
        list.add(CategoryContent.Add())

        view.recyclerView_category.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        val adapter = CategoryAdapter(list)
        view.recyclerView_category.adapter = adapter

        var filterDateRange: FilterDateRange = FilterDateRange.TODAY
        val rangeAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item,
            FilterDateRange.getList())
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.spinnerDate.setAdapter(rangeAdapter)
        view.spinnerDate.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterDateRange = FilterDateRange.getType(p2)
            }

        })

        view.btn_apply.setOnClickListener {
            viewModel.filterList(adapter.list, filterDateRange, onDone = Action {
                popupWindow.dismiss()
            })

        }

        popupWindow.showAsDropDown(binding.btnFilter)
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

    override fun onUpdateExpenses() {
        getAll()
    }

    override fun onSelectedExpense(expenseContent: ExpenseContent.Item) {
        SaveExpenseDialogFragment.newInstance(expenseContent.expense, this).show(childFragmentManager,"SaveExpenseDialogFragment")

    }
}