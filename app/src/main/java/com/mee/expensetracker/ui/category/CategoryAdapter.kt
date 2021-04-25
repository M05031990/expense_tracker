package com.mee.expensetracker.ui.category

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
import com.mee.expensetracker.R
import com.mee.expensetracker.model.DefaultCategory
import com.mee.expensetracker.model.IncomeTimeRange
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.layout_add_category_button.view.*
import kotlinx.android.synthetic.main.layout_category_item.view.*

/**
 * Created by Michelle Dayangco
 */
class CategoryAdapter constructor(val list: MutableList<CategoryContent>) :
    RecyclerView.Adapter<CategoryAdapter.BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (ItemType.getType(viewType)) {
            ItemType.ITEM -> return ItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_category_item, parent, false)
            )
            ItemType.ADD -> return AddViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_add_category_button, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val content = list.get(position)
        if (content is CategoryContent.Item)
            return ItemType.ITEM.ordinal
        return ItemType.ADD.ordinal
    }

    enum class ItemType {
        ITEM, ADD;

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

    fun update(str: String) {
        list.removeIf { it is CategoryContent.Add }
        list.add(CategoryContent.Item(str))
        list.add(CategoryContent.Add())
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        holder.bind(list.get(position))
    }

    open abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(categoryContent: CategoryContent)
    }

    inner class ItemViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(categoryContent: CategoryContent) {
            val content = categoryContent as CategoryContent.Item
            itemView.edit_item.setText(content.content)
        }

    }

    inner class AddViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bind(categoryContent: CategoryContent) {
            itemView.btn_category.setOnClickListener {
                popMenu(onMenuClick = Consumer {
                    val title = it.title.toString()
                    val exist = list.any {
                        if (it is CategoryContent.Item)
                            it.content.equals(title)
                        else
                            false
                    }
                    if (exist){
                        Toast.makeText(itemView.context,"category already added.", Toast.LENGTH_SHORT).show()
                    }else update(title)
                })
            }
        }

        fun popMenu(onMenuClick: Consumer<MenuItem>) {
            val popup = PopupMenu(itemView.context, itemView.btn_category)
            DefaultCategory.getList().forEach {
                popup.menu.add(it)
            }
            popup.setOnMenuItemClickListener { item ->
                onMenuClick.accept(item)
                true
            }

            popup.show()
        }
    }

}