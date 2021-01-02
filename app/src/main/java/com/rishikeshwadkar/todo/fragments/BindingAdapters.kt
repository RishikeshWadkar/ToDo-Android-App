package com.rishikeshwadkar.todo.fragments

import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rishikeshwadkar.todo.R
import com.rishikeshwadkar.todo.data.models.Priority
import com.rishikeshwadkar.todo.data.models.ToDoData
import com.rishikeshwadkar.todo.fragments.list.ListFragmentDirections

class BindingAdapters {

    companion object {

        @BindingAdapter("navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean){
            view.setOnClickListener {
                if (navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }

        }

        @BindingAdapter("android:parsePriority")
        @JvmStatic
        fun parsePriority(spinner: Spinner, priority: Priority){
            when(priority){
                Priority.HIGH -> spinner.setSelection(0)
                Priority.MEDIUM -> spinner.setSelection(1)
                Priority.LOW -> spinner.setSelection(2)
            }
        }

        @BindingAdapter("parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(Button: AppCompatButton, priority: Priority){
            when(priority){
                Priority.HIGH -> Button.setBackgroundResource(R.drawable.item_priority_red)
                Priority.MEDIUM -> Button.setBackgroundResource(R.drawable.item_priority_yellow)
                Priority.LOW -> Button.setBackgroundResource(R.drawable.item_priority_green)
            }
        }

        @BindingAdapter("onViewClicked")
        @JvmStatic
        fun onViewClicked(constraintLayout: ConstraintLayout, currentItem: ToDoData){
            constraintLayout.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                Navigation.findNavController(constraintLayout).navigate(action)
            }
        }
    }
}