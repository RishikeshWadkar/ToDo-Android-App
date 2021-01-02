package com.rishikeshwadkar.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rishikeshwadkar.todo.R
import com.rishikeshwadkar.todo.data.models.ToDoData
import com.rishikeshwadkar.todo.data.viewmodel.ToDoViewModel
import com.rishikeshwadkar.todo.databinding.FragmentListBinding
import com.rishikeshwadkar.todo.fragments.list.adapters.ListAdapter
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        setHasOptionsMenu(true)

        // setup recyclerView
        setupRecyclerView()

        //LiveData observer
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
            if (data.isEmpty()){
                binding.noDataTextView.visibility = View.VISIBLE
                binding.noDataImageView.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

// <=================================================================================================>

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        val layoutManager = GridLayoutManager(requireActivity(),2)

        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = adapter
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Delete Item
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteData(deletedItem)
                //adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // Restore Item
                restoreItem(requireView(), deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun restoreItem(view: View, deletedItem: ToDoData, position: Int){
        val snackBar = Snackbar.make(view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)

        }
        snackBar.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_item_deleteAll -> {
                confirmDialog()
            }
            R.id.list_item_priority_high -> {
                mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            }
            R.id.list_item_priority_low -> {
                mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_fragment_menu,menu)

        val search = menu.findItem(R.id.list_item_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        // if user click yes then do this
        builder.setPositiveButton("Yes"){_, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),
                    "Successfully Removed...",
                    Toast.LENGTH_SHORT).show()
        }
        // if user clicks no then do this (nothing)
        builder.setNegativeButton("No"){_, _ ->}
        // setup builder
        builder.setTitle("Delete All")
        builder.setMessage("Are you sure you want to delete all")
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }
    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })
    }
}