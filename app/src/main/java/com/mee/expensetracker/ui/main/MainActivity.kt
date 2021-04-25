package com.mee.expensetracker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.carded.api.ExpenseContent
import com.mee.expensetracker.R
import com.mee.expensetracker.base.BaseActivity
import com.mee.expensetracker.databinding.ActivityMainBinding
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.ui.expense.ExpenseAdapter
import com.mee.expensetracker.ui.expense.SaveExpenseDialogFragment
import com.mee.expensetracker.ui.settings.SettingActivity
import com.mee.expensetracker.ui.start.SourceIncomeDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_my_day.*

@AndroidEntryPoint
class MainActivity : BaseActivity(), SaveExpenseDialogFragment.SaveExpenseDialogFragmentListener,
    ExpenseAdapter.Callback {
    lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()
    var adapter: ExpenseAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpenseAdapter(mutableListOf(), this)
        binding.recyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            SaveExpenseDialogFragment.newInstance().show(supportFragmentManager,"SaveExpenseDialogFragment")
        }

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        getAll()
        viewModel.expenseContents.observe(this, Observer {
            adapter?.list = it.toMutableList()
            adapter?.notifyDataSetChanged()
        })

        layoutMyDayMain.setOnClickListener {
            viewModel.parseExpenseContentForMyDay(Consumer {
                txt_Total.text = ""+it
            })
        }
        btn_refresh.setOnClickListener {
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
        SaveExpenseDialogFragment.newInstance(expenseContent.expense).show(supportFragmentManager,"SaveExpenseDialogFragment")

    }
}