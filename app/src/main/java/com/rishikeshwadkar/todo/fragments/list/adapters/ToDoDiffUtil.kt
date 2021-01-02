package com.rishikeshwadkar.todo.fragments.list.adapters

import androidx.recyclerview.widget.DiffUtil
import com.rishikeshwadkar.todo.data.models.ToDoData

class ToDoDiffUtil(private val oldData: List<ToDoData>,
                   private val newData: List<ToDoData>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] === newData[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
                && oldData[oldItemPosition].title == newData[newItemPosition].title
                && oldData[oldItemPosition].description == newData[newItemPosition].description
                && oldData[oldItemPosition].priority == newData[newItemPosition].priority
    }

}