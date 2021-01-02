package com.rishikeshwadkar.todo.fragments.add

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rishikeshwadkar.todo.R
import com.rishikeshwadkar.todo.data.models.Priority
import com.rishikeshwadkar.todo.data.models.ToDoData
import com.rishikeshwadkar.todo.data.viewmodel.ToDoViewModel
import com.rishikeshwadkar.todo.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.Console

class AddFragment : Fragment() {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)
        view.add_spinner.onItemSelectedListener = mSharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_checkBTN){
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val mTitle: String = add_title_TV.text.toString()
        val mPriority: String = add_spinner.selectedItem.toString()

        val mDescription: String = add_multiline_TV.text.toString()

        val validation = mSharedViewModel.verifyData(mTitle, mDescription)

        if(validation){
            //insert data
            val newData = ToDoData(
                    0,
                    mTitle,
                    mSharedViewModel.parsePriority(mPriority),
                    mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully Inserted!!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else{
            Toast.makeText(requireContext(), "Insert Valid Data...", Toast.LENGTH_SHORT).show()
        }
    }
}