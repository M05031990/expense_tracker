package com.mee.expensetracker.ui.expense

import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.carded.api.CategoryContent
import com.carded.api.ExpenseContent
import com.mee.expensetracker.R
import com.mee.expensetracker.model.DefaultCategory
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.IncomeTimeRange
import com.mee.expensetracker.ui.datepicker.DateTimeDialogFragment
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_expense_header.view.*
import kotlinx.android.synthetic.main.layout_expense_item.view.*

/**
 * Created by Michelle Dayangco
 */
class ExpenseAdapter constructor(var list: MutableList<ExpenseContent>, val callback: Callback) :
    RecyclerView.Adapter<ExpenseAdapter.BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (ItemType.getType(viewType)) {
            ItemType.ITEM -> return ItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_expense_item, parent, false)
            )
            ItemType.HEADER -> return HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_expense_header, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val content = list.get(position)
        if (content is ExpenseContent.Item)
            return ItemType.ITEM.ordinal
        return ItemType.HEADER.ordinal
    }

    enum class ItemType {
        ITEM, HEADER;

        companion object {

            fun getType(order: Int): ItemType {
                values().forEach {
                    if (it.ordinal == order)
                        return it
                }
                return ITEM
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        holder.bind(list.get(position))
    }

    interface Callback{
       fun onSelectedExpense(expenseContent: ExpenseContent.Item)
    }

    open abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(context: ExpenseContent)
    }

    inner class ItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(content: ExpenseContent) {
            if (content is ExpenseContent.Item){
                val expense = content.expense
                itemView.txt_title.text= expense.title
                itemView.txt_desc.text = expense.description
                itemView.txt_category.text = getCategories(expense)
                itemView.txt_amount.text = ""+expense.amount
                itemView.txt_date.text = DateTimeDialogFragment.formateDateToString(expense.create_at)

            }

            itemView.setOnClickListener {
                callback.onSelectedExpense(content as ExpenseContent.Item)
            }
        }

        fun getCategories(expense: Expense): String{
            var categoryStr = ""
            expense.categories?.forEachIndexed { index, s ->
                categoryStr = categoryStr+s

                if (index < (expense.categories?.size!!-1)){
                    categoryStr = categoryStr+", "
                }
            }
            return categoryStr
        }

    }

    inner class HeaderViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(content: ExpenseContent) {
            if (content is ExpenseContent.Header){
                itemView.txt_header.text = DateTimeDialogFragment.formateDateToString(content.date)
            }
        }
    }



}