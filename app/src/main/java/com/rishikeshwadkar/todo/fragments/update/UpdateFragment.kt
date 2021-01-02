package com.rishikeshwadkar.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.rishikeshwadkar.todo.R
import com.rishikeshwadkar.todo.data.models.Priority
import com.rishikeshwadkar.todo.data.models.ToDoData
import com.rishikeshwadkar.todo.data.viewmodel.ToDoViewModel
import com.rishikeshwadkar.todo.databinding.FragmentUpdateBinding
import com.rishikeshwadkar.todo.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args: UpdateFragmentArgs by navArgs()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.args = args
        binding.currentSpinner.onItemSelectedListener = mSharedViewModel.listener

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mTitle = currentEditText.text.toString()
        val mDescription = current_description.text.toString()
        val mPriority = current_spinner.selectedItem.toString()

        if (item.itemId == (R.id.update_item_save)){
            val validate = mSharedViewModel.verifyData(mTitle,mDescription)

            if(validate){
                updateData(mTitle,mPriority,mDescription)
                Toast.makeText(requireContext(), "Data Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
        }
        else if(item.itemId == R.id.update_item_delete){
            confirmDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmDialog() {

        val builder = AlertDialog.Builder(requireContext())

        // if user click yes then do this
        builder.setPositiveButton("Yes"){_, _ ->
            mToDoViewModel.deleteData(args.currentItem)
            Toast.makeText(requireContext(),
                    "Successfully Removed '${args.currentItem.title.toString()}'",
                    Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        // if user clicks no then do this (nothing)
        builder.setNegativeButton("No"){_, _ ->}

        // setup builder
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title.toString()}'")
        builder.create().show()
    }

    private fun updateData(title: String, priority: String, description: String) {
        val updatedData = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(priority),
                description
        )
        mToDoViewModel.updateData(updatedData)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

}